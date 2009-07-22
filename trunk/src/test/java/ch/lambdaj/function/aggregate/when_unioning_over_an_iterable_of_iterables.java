package ch.lambdaj.function.aggregate;

import java.util.*;

import ch.lambdaj.function.convert.*;
import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.relational.Relational.*;

import org.junit.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.*;

public class when_unioning_over_an_iterable_of_iterables {

    private ICompilationUnit compilationUnit;
    private ICompilationUnit[] units;
    private ICompilationUnit[] argument;
    private IPackageFragment fragment;
    private IPackageFragment[] fragments;
    private Iterable<ICompilationUnit> collected;
    private Iterable<Iterable<ICompilationUnit>> collectedUnitArrays;
    private ICompilationUnit anotherCompilationUnit;
    private ICompilationUnit[] moreUnits;
    private ICompilationUnit[] allUnits;
    private IPackageFragment anotherFragment;

    @Before
    public void establish_context() {
        compilationUnit = mock(ICompilationUnit.class);
        anotherCompilationUnit = mock(ICompilationUnit.class);
        units = new ICompilationUnit[] { compilationUnit };
        moreUnits = new ICompilationUnit[] { anotherCompilationUnit };

        allUnits = new ICompilationUnit[] { compilationUnit, anotherCompilationUnit };


        fragment = mock(IPackageFragment.class);
        when(fragment.getCompilationUnits()).thenReturn(units);
        anotherFragment = mock(IPackageFragment.class);
        when(anotherFragment.getCompilationUnits()).thenReturn(moreUnits);
        fragments = new IPackageFragment[] { fragment, anotherFragment };

        collectedUnitArrays = convert(
                collect(Arrays.asList(fragments), on(IPackageFragment.class).getCompilationUnits()),
                new Converter<ICompilationUnit[], Iterable<ICompilationUnit>>() {
                    public Iterable<ICompilationUnit> convert(final ICompilationUnit[] from) {
                        return Arrays.asList(from);
                    }
                });

        collected = union(collectedUnitArrays);
    }

    @Test public void should_union_all_iterables_into_one_iterable() {
        assertThat(collected, hasItems(allUnits));
    }

    public interface ICompilationUnit {
    }

    public interface IPackageFragment {
        ICompilationUnit[] getCompilationUnits();
    }
}
