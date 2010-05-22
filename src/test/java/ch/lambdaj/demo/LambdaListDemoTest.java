package ch.lambdaj.demo;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static ch.lambdaj.collection.LambdaCollections.with;
import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.demo.Util.listsAreEqual;

import java.util.*;

public class LambdaListDemoTest {

    private final Db db = Db.getInstance();

    @Test
    public void testPrintAllBrands() {
        StringBuilder sb = new StringBuilder();
        for (Car car : db.getCars())
            sb.append(car.getBrand()).append(", ");
        String brandsIterative = sb.toString().substring(0, sb.length()-2);

        String brands = with(db.getCars()).extract(on(Car.class).getBrand()).join();

        assertEquals(brandsIterative, brands);
    }

    @Test
    public void testFindAllSalesOfAFerrari() {
        List<Sale> salesIterative = new ArrayList<Sale>();
        for (Sale sale : db.getSales()) {
            if (sale.getCar().getBrand().equals("Ferrari")) salesIterative.add(sale);
        }

        List<Sale> sales = with(db.getSales()).filter(having(on(Sale.class).getCar().getBrand(), equalTo("Ferrari")));

        assertTrue(listsAreEqual(sales, salesIterative));
    }

    @Test
    public void testFindAllBuysOfYoungestPerson() {
        Person youngest = null;
        for (Person person : db.getPersons()) {
            if (youngest == null || person.getAge() < youngest.getAge()) youngest = person;
        }
        List<Sale> salesIterative = new ArrayList<Sale>();
        for (Sale sale : db.getSales()) {
            if (sale.getBuyer().equals(youngest)) salesIterative.add(sale);
        }

        Person lambdaYoungest = with(db.getPersons()).selectMin(on(Person.class).getAge());
        List<Sale> sales = with(db.getSales()).filter(having(on(Sale.class).getBuyer(), equalTo(lambdaYoungest)));

        assertTrue(listsAreEqual(sales, salesIterative));
    }

    @Test
    public void testFindMostCostlySaleValue() {
        double maxCost = 0.0;
        for (Sale sale : db.getSales()) {
            double cost = sale.getCost();
            if (cost > maxCost) maxCost = cost;
        }

        double max = with(db.getSales()).max(on(Sale.class).getCost());
        assertEquals(max, maxCost, .001);
    }

    @Test
    public void testSumSalesCostWhereBothActorsAreAMale() {
        double sumIterative = 0.0;
        for (Sale sale : db.getSales()) {
            if (sale.getBuyer().isMale() && sale.getSeller().isMale())
                sumIterative += sale.getCost();
        }

        double sum = with(db.getSales()).filter(having(on(Sale.class).getBuyer().isMale()).and(having(on(Sale.class).getSeller().isMale()))).sum(on(Sale.class).getCost());
        assertEquals(sum, sumIterative, .001);

        double sum2 = with(db.getSales()).filter(having(on(Sale.class).getBuyer().isMale())).filter(having(on(Sale.class).getSeller().isMale())).sum(on(Sale.class).getCost());
        assertEquals(sum2, sumIterative, .001);
    }

    @Test
    public void testFindYoungestAgeOfWhoBoughtACarForMoreThan50000() {
        int ageIterative = Integer.MAX_VALUE;
        for (Sale sale : db.getSales()) {
            if (sale.getCost() > 50000.00) {
                int buyerAge = sale.getBuyer().getAge();
                if (buyerAge < ageIterative) ageIterative = buyerAge;
            }
        }

        int age = min(with(db.getSales()).foreach(having(on(Sale.class).getCost(), greaterThan(50000.00))).getBuyer(), on(Person.class).getAge());

        assertEquals(age, ageIterative);
    }
}
