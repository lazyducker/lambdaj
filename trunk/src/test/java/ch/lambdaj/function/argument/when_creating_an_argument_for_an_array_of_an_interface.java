package ch.lambdaj.function.argument;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectFirst;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import org.junit.Before;
import org.junit.Test;

import ch.lambdaj.function.argument.ArgumentsFactory;
import ch.lambdaj.proxy.nodefconstructor.ElementId;
import ch.lambdaj.proxy.nodefconstructor.RelationMetricResult;

import static ch.lambdaj.Lambda.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;

public class when_creating_an_argument_for_an_array_of_an_interface {

    private ICompilationUnit compilationUnit;
    private ICompilationUnit[] units;
    private ICompilationUnit[] argument;

    @Before
    public void establish_context() {
        compilationUnit = mock(ICompilationUnit.class);
        units = new ICompilationUnit[] { compilationUnit };

        argument = ArgumentsFactory.createArgument(units.getClass());
    }

    @Test
    public void should_not_throw() {
        assertThat(argument, is(instanceOf(ICompilationUnit[].class)));
    }

    public interface ICompilationUnit {

    }

    public interface IPackageFragment {
        ICompilationUnit[] getCompilationUnits();
    }
}
