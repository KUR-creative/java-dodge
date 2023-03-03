// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.runner;

import org.junit.runner.manipulation.Filter;
import java.util.Iterator;
import org.junit.runners.model.InitializationError;
import org.junit.internal.Classes;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

class JUnitCommandLineParseResult
{
    private final List<String> filterSpecs;
    private final List<Class<?>> classes;
    private final List<Throwable> parserErrors;
    
    JUnitCommandLineParseResult() {
        this.filterSpecs = new ArrayList<String>();
        this.classes = new ArrayList<Class<?>>();
        this.parserErrors = new ArrayList<Throwable>();
    }
    
    public List<String> getFilterSpecs() {
        return Collections.unmodifiableList((List<? extends String>)this.filterSpecs);
    }
    
    public List<Class<?>> getClasses() {
        return Collections.unmodifiableList((List<? extends Class<?>>)this.classes);
    }
    
    public static JUnitCommandLineParseResult parse(final String[] args) {
        final JUnitCommandLineParseResult result = new JUnitCommandLineParseResult();
        result.parseArgs(args);
        return result;
    }
    
    private void parseArgs(final String[] args) {
        this.parseParameters(this.parseOptions(args));
    }
    
    String[] parseOptions(final String... args) {
        for (int i = 0; i != args.length; ++i) {
            final String arg = args[i];
            if (arg.equals("--")) {
                return this.copyArray(args, i + 1, args.length);
            }
            if (!arg.startsWith("--")) {
                return this.copyArray(args, i, args.length);
            }
            if (arg.startsWith("--filter=") || arg.equals("--filter")) {
                String filterSpec;
                if (arg.equals("--filter")) {
                    if (++i >= args.length) {
                        this.parserErrors.add(new CommandLineParserError(arg + " value not specified"));
                        break;
                    }
                    filterSpec = args[i];
                }
                else {
                    filterSpec = arg.substring(arg.indexOf(61) + 1);
                }
                this.filterSpecs.add(filterSpec);
            }
            else {
                this.parserErrors.add(new CommandLineParserError("JUnit knows nothing about the " + arg + " option"));
            }
        }
        return new String[0];
    }
    
    private String[] copyArray(final String[] args, final int from, final int to) {
        final ArrayList<String> result = new ArrayList<String>();
        for (int j = from; j != to; ++j) {
            result.add(args[j]);
        }
        return result.toArray(new String[result.size()]);
    }
    
    void parseParameters(final String[] args) {
        for (final String arg : args) {
            try {
                this.classes.add(Classes.getClass(arg));
            }
            catch (ClassNotFoundException e) {
                this.parserErrors.add(new IllegalArgumentException("Could not find class [" + arg + "]", e));
            }
        }
    }
    
    private Request errorReport(final Throwable cause) {
        return Request.errorReport(JUnitCommandLineParseResult.class, cause);
    }
    
    public Request createRequest(final Computer computer) {
        if (this.parserErrors.isEmpty()) {
            final Request request = Request.classes(computer, (Class<?>[])this.classes.toArray(new Class[this.classes.size()]));
            return this.applyFilterSpecs(request);
        }
        return this.errorReport(new InitializationError(this.parserErrors));
    }
    
    private Request applyFilterSpecs(Request request) {
        try {
            for (final String filterSpec : this.filterSpecs) {
                final Filter filter = FilterFactories.createFilterFromFilterSpec(request, filterSpec);
                request = request.filterWith(filter);
            }
            return request;
        }
        catch (FilterFactory.FilterNotCreatedException e) {
            return this.errorReport(e);
        }
    }
    
    public static class CommandLineParserError extends Exception
    {
        private static final long serialVersionUID = 1L;
        
        public CommandLineParserError(final String message) {
            super(message);
        }
    }
}
