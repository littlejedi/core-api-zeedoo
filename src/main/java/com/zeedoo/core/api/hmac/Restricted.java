package com.zeedoo.core.api.hmac;

import java.lang.annotation.*;

/**
 * Annotation to indicate a resource is protected (needs HMAC Authentication)
 *         
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Restricted {
}