package com.exalt.healthcare.infrastructure.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging execution of service and repository Spring components.
 * @author Ramesh Fadatare
 *
 */
@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Pointcut for all service methods.
     */
    @Pointcut("within(com.exalt.healthcare.domain.service.implementations..*)")
    public void serviceLayer() {}

    /**
     * Pointcut for appointment-related methods.
     */
    @Pointcut("execution(* com.exalt.healthcare.domain.service.implementations.AppointmentServiceImpl.*(..))")
    public void appointmentMethods() {}

    /**
     * Pointcut for prescription-related methods.
     */
    @Pointcut("execution(* com.exalt.healthcare.domain.service.implementations.PrescriptionServiceImpl.*(..))")
    public void prescriptionMethods() {}

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(com.exalt.healthcare..*)" +
            " || within(com.exalt.healthcare.domain.service.implementations..*)" +
            " || within(com.exalt.healthcare.presentation.controller..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Logs exceptions thrown in service layer.
     */
    @AfterThrowing(pointcut = "serviceLayer()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
    }

    /**
     * Logs appointment booking and cancellation events.
     */
    @AfterReturning(pointcut = "appointmentMethods()", returning = "result")
    public void logAppointmentEvents(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().getName();
        if (method.contains("book") || method.contains("cancel")) {
            log.info("Appointment {} executed with arguments = {} and result = {}",
                    method, Arrays.toString(joinPoint.getArgs()), result);
        }
        // You can add logic here for double-booking detection if your service returns such info
        if ("bookAppointment".equals(method) && result != null && result.toString().contains("DoubleBooking")) {
            log.warn("Double booking attempt prevented: {}", Arrays.toString(joinPoint.getArgs()));
        }
    }

    /**
     * Logs prescription creation/update events.
     */
    @AfterReturning(pointcut = "prescriptionMethods()", returning = "result")
    public void logPrescriptionEvents(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().getName();
        if (method.contains("add") || method.contains("update")) {
            log.info("Prescription {} executed with arguments = {} and result = {}",
                    method, Arrays.toString(joinPoint.getArgs()), result);
        }
    }

    /**
     * Optional: General around advice for debugging other service methods.
     */
    @Around("serviceLayer()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with arguments = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()",
                    Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
            throw e;
        }
    }
}
