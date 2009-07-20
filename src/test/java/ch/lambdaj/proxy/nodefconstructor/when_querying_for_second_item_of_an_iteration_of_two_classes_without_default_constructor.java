package ch.lambdaj.proxy.nodefconstructor;
 import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.function.matcher.HasArgumentWithValue.*;
import static ch.lambdaj.function.matcher.HasNestedPropertyWithValue.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class when_querying_for_second_item_of_an_iteration_of_two_classes_without_default_constructor {

    private RelationMetricResult selected;
    private RelationMetricResult second_suspect;
    private RelationMetricResult first_suspect; 

    @Before
    public void establish_context() {
        first_suspect = new RelationMetricResult(new ElementId("1"), new ElementId("2"), 1.0);
        second_suspect = new RelationMetricResult(new ElementId("2"), new ElementId("3"), 2.0);
        Iterable<RelationMetricResult> suspects = Arrays.asList(first_suspect, second_suspect);
        
        selected = selectFirst(suspects, 
                having(on(RelationMetricResult.class).from().intValue(), 
                equalTo(2)));
    }
    
    @Test
    public void should_not_throw_and_return() {
        assert(second_suspect.equals(selected));
    }
}


