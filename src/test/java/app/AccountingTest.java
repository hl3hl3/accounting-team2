package app;

import static org.junit.Assert.*;

import app.repository.IBudgetRepo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class AccountingTest {

    private Accounting accounting;
    private FakeBudgetRepo fakeRepo = new FakeBudgetRepo();

    @Test
    public void fullMonth() {
        setFakeRepo(new Budget("201904",3000));

        LocalDate start = LocalDate.of(2019, 4, 1);
        LocalDate end = LocalDate.of(2019, 4, 30);
        amountShouldBe(start, end, 3000);
    }

    @Test
    public void OneDay() {
        setFakeRepo(new Budget("201904",3000));

        LocalDate start = LocalDate.of(2019, 4, 1);
        LocalDate end = LocalDate.of(2019, 4, 1);
        amountShouldBe(start, end, 100);
    }

    @Test
    public void TwoDays() {
        setFakeRepo(new Budget("201904",3000));

        LocalDate start = LocalDate.of(2019, 4, 1);
        LocalDate end = LocalDate.of(2019, 4, 2);
        amountShouldBe(start, end, 200);
    }

    @Test
    public void ThreeDays() {
        setFakeRepo(new Budget("201904",3000));

        LocalDate start = LocalDate.of(2019, 4, 1);
        LocalDate end = LocalDate.of(2019, 4, 3);
        amountShouldBe(start, end, 300);
    }


    @Test
    public void noBudget() {
        setFakeRepo();

        LocalDate start = LocalDate.of(2019, 3, 1);
        LocalDate end = LocalDate.of(2019, 3, 10);
        amountShouldBe(start, end, 0);
    }

    @Test
    public void CrossMonths() {
        setFakeRepo(
                new Budget("201901",3100),
                new Budget("201902",1400)
        );

        LocalDate start = LocalDate.of(2019, 1, 31);
        LocalDate end = LocalDate.of(2019, 2, 1);
        amountShouldBe(start, end, 150);
    }

    @Test
    public void CrossNoBudgetMonths() {
        setFakeRepo(
                new Budget("201902",1400),
                new Budget("201903",3100),
                new Budget("201904",3000)
        );

        LocalDate start = LocalDate.of(2019, 2, 1);
        LocalDate end = LocalDate.of(2019, 4, 1);
        amountShouldBe(start, end, 4600);
    }

    @Test
    public void CrossYears() {
        setFakeRepo(
                new Budget("201812",3100),
                new Budget("201901",3100)
        );

        LocalDate start = LocalDate.of(2018, 12, 31);
        LocalDate end = LocalDate.of(2019, 1, 1);
        amountShouldBe(start, end, 200);
    }

    @Test
    public void ErrorDate() {
        setFakeRepo(
                new Budget("201812",3100),
                new Budget("201901",3100),
                new Budget("201902",2800),
                new Budget("201904",3000),
                new Budget("201912",3100)
        );

        LocalDate start = LocalDate.of(2019, 4, 1);
        LocalDate end = LocalDate.of(2019, 2, 1);
        amountShouldBe(start, end, 0);
    }

    private void amountShouldBe(LocalDate start, LocalDate end, int i) {
        assertEquals(i, accounting.totalAmount(start, end), 0.00);
    }

    private void setFakeRepo(Budget... data) {
        accounting = new Accounting(
                fakeRepo.withFakeData(
                        (data == null || data.length == 0) ?
                                new ArrayList<>() : Arrays.asList(data)
                ));
    }

    class FakeBudgetRepo implements IBudgetRepo {

        private List<Budget> fakeData;

        @Override
        public List<Budget> getAll() {
            return fakeData;
        }

        public FakeBudgetRepo withFakeData(List<Budget> fakeData) {
            this.fakeData = fakeData;
            return this;
        }
    }
}






