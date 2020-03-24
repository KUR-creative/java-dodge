// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.internal.runners.rules;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.Rule;
import org.junit.ClassRule;
import java.util.Iterator;
import org.junit.runners.model.FrameworkMember;
import org.junit.runners.model.TestClass;
import java.util.List;
import java.lang.annotation.Annotation;

public class RuleMemberValidator
{
    public static final RuleMemberValidator CLASS_RULE_VALIDATOR;
    public static final RuleMemberValidator RULE_VALIDATOR;
    public static final RuleMemberValidator CLASS_RULE_METHOD_VALIDATOR;
    public static final RuleMemberValidator RULE_METHOD_VALIDATOR;
    private final Class<? extends Annotation> annotation;
    private final boolean methods;
    private final List<RuleValidator> validatorStrategies;
    
    RuleMemberValidator(final Builder builder) {
        this.annotation = builder.annotation;
        this.methods = builder.methods;
        this.validatorStrategies = builder.validators;
    }
    
    public void validate(final TestClass target, final List<Throwable> errors) {
        final List<? extends FrameworkMember<?>> members = (List<? extends FrameworkMember<?>>)(this.methods ? target.getAnnotatedMethods(this.annotation) : target.getAnnotatedFields(this.annotation));
        for (final FrameworkMember<?> each : members) {
            this.validateMember(each, errors);
        }
    }
    
    private void validateMember(final FrameworkMember<?> member, final List<Throwable> errors) {
        for (final RuleValidator strategy : this.validatorStrategies) {
            strategy.validate(member, this.annotation, errors);
        }
    }
    
    private static Builder classRuleValidatorBuilder() {
        return new Builder((Class)ClassRule.class);
    }
    
    private static Builder testRuleValidatorBuilder() {
        return new Builder((Class)Rule.class);
    }
    
    private static boolean isRuleType(final FrameworkMember<?> member) {
        return isMethodRule(member) || isTestRule(member);
    }
    
    private static boolean isTestRule(final FrameworkMember<?> member) {
        return TestRule.class.isAssignableFrom(member.getType());
    }
    
    private static boolean isMethodRule(final FrameworkMember<?> member) {
        return MethodRule.class.isAssignableFrom(member.getType());
    }
    
    static {
        CLASS_RULE_VALIDATOR = classRuleValidatorBuilder().withValidator(new DeclaringClassMustBePublic()).withValidator(new MemberMustBeStatic()).withValidator(new MemberMustBePublic()).withValidator(new FieldMustBeATestRule()).build();
        RULE_VALIDATOR = testRuleValidatorBuilder().withValidator(new MemberMustBeNonStaticOrAlsoClassRule()).withValidator(new MemberMustBePublic()).withValidator(new FieldMustBeARule()).build();
        CLASS_RULE_METHOD_VALIDATOR = classRuleValidatorBuilder().forMethods().withValidator(new DeclaringClassMustBePublic()).withValidator(new MemberMustBeStatic()).withValidator(new MemberMustBePublic()).withValidator(new MethodMustBeATestRule()).build();
        RULE_METHOD_VALIDATOR = testRuleValidatorBuilder().forMethods().withValidator(new MemberMustBeNonStaticOrAlsoClassRule()).withValidator(new MemberMustBePublic()).withValidator(new MethodMustBeARule()).build();
    }
    
    private static class Builder
    {
        private final Class<? extends Annotation> annotation;
        private boolean methods;
        private final List<RuleValidator> validators;
        
        private Builder(final Class<? extends Annotation> annotation) {
            this.annotation = annotation;
            this.methods = false;
            this.validators = new ArrayList<RuleValidator>();
        }
        
        Builder forMethods() {
            this.methods = true;
            return this;
        }
        
        Builder withValidator(final RuleValidator validator) {
            this.validators.add(validator);
            return this;
        }
        
        RuleMemberValidator build() {
            return new RuleMemberValidator(this);
        }
    }
    
    private static final class MemberMustBeNonStaticOrAlsoClassRule implements RuleValidator
    {
        public void validate(final FrameworkMember<?> member, final Class<? extends Annotation> annotation, final List<Throwable> errors) {
            final boolean isMethodRuleMember = isMethodRule(member);
            final boolean isClassRuleAnnotated = member.getAnnotation(ClassRule.class) != null;
            if (member.isStatic() && (isMethodRuleMember || !isClassRuleAnnotated)) {
                String message;
                if (isMethodRule(member)) {
                    message = "must not be static.";
                }
                else {
                    message = "must not be static or it must be annotated with @ClassRule.";
                }
                errors.add(new ValidationError(member, annotation, message));
            }
        }
    }
    
    private static final class MemberMustBeStatic implements RuleValidator
    {
        public void validate(final FrameworkMember<?> member, final Class<? extends Annotation> annotation, final List<Throwable> errors) {
            if (!member.isStatic()) {
                errors.add(new ValidationError(member, annotation, "must be static."));
            }
        }
    }
    
    private static final class DeclaringClassMustBePublic implements RuleValidator
    {
        public void validate(final FrameworkMember<?> member, final Class<? extends Annotation> annotation, final List<Throwable> errors) {
            if (!this.isDeclaringClassPublic(member)) {
                errors.add(new ValidationError(member, annotation, "must be declared in a public class."));
            }
        }
        
        private boolean isDeclaringClassPublic(final FrameworkMember<?> member) {
            return Modifier.isPublic(member.getDeclaringClass().getModifiers());
        }
    }
    
    private static final class MemberMustBePublic implements RuleValidator
    {
        public void validate(final FrameworkMember<?> member, final Class<? extends Annotation> annotation, final List<Throwable> errors) {
            if (!member.isPublic()) {
                errors.add(new ValidationError(member, annotation, "must be public."));
            }
        }
    }
    
    private static final class FieldMustBeARule implements RuleValidator
    {
        public void validate(final FrameworkMember<?> member, final Class<? extends Annotation> annotation, final List<Throwable> errors) {
            if (!isRuleType(member)) {
                errors.add(new ValidationError(member, annotation, "must implement MethodRule or TestRule."));
            }
        }
    }
    
    private static final class MethodMustBeARule implements RuleValidator
    {
        public void validate(final FrameworkMember<?> member, final Class<? extends Annotation> annotation, final List<Throwable> errors) {
            if (!isRuleType(member)) {
                errors.add(new ValidationError(member, annotation, "must return an implementation of MethodRule or TestRule."));
            }
        }
    }
    
    private static final class MethodMustBeATestRule implements RuleValidator
    {
        public void validate(final FrameworkMember<?> member, final Class<? extends Annotation> annotation, final List<Throwable> errors) {
            if (!isTestRule(member)) {
                errors.add(new ValidationError(member, annotation, "must return an implementation of TestRule."));
            }
        }
    }
    
    private static final class FieldMustBeATestRule implements RuleValidator
    {
        public void validate(final FrameworkMember<?> member, final Class<? extends Annotation> annotation, final List<Throwable> errors) {
            if (!isTestRule(member)) {
                errors.add(new ValidationError(member, annotation, "must implement TestRule."));
            }
        }
    }
    
    interface RuleValidator
    {
        void validate(final FrameworkMember<?> p0, final Class<? extends Annotation> p1, final List<Throwable> p2);
    }
}
