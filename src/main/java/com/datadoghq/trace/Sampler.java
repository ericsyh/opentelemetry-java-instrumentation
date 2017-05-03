package com.datadoghq.trace;


import io.opentracing.Span;

/**
 * Main interface to sample a collection of traces.
 */
public interface Sampler {

    /**
     * Sample a collection of traces based on the parent span
     *
     * @param span the parent span with its context
     * @return true when the trace/spans has to be reported/written
     */
    boolean sample(Span span);

}
