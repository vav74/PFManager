import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class StatTest {
    static Stat stat;

    @BeforeAll
    static void setUpApp() throws IOException, CsvException, ClassNotFoundException {
        stat = new Stat();
        stat.purchaseList=new ArrayList<>();
        System.out.println("Записи очищены");
        Stat.file=new File("TestFile.bin");
    }

    @AfterAll
    static void endApp(){
        System.out.println(Stat.file.getName()+" удален: "+ Stat.file.delete());
    }

    @ParameterizedTest
    @MethodSource("getArguments")
    @DisplayName("Тест maxCategory")
    void getMaxCatTest(String p, MaxCategory expected) throws IOException {
        stat.addNewPurchase(p);
        MaxCategory result = stat.maxCategory();
        Assertions.assertEquals(result, expected);
    }

    private static Stream<Arguments> getArguments() {
        return Stream.of(
                Arguments.of("{\"title\":\"булка\",\"date\":\"2022.01.01\",\"sum\":10}", new MaxCategory("food", 10)),
                Arguments.of("{\"title\":\"колбаса\",\"date\":\"2022.01.01\",\"sum\":20}", new MaxCategory("food", 30)),
                Arguments.of("{\"title\":\"сухарики\",\"date\":\"2022.01.01\",\"sum\":30}", new MaxCategory("food", 60)),
                Arguments.of("{\"title\":\"курица\",\"date\":\"2022.01.01\",\"sum\":40}", new MaxCategory("food", 100)),
                Arguments.of("{\"title\":\"тапки\",\"date\":\"2022.01.01\",\"sum\":50}", new MaxCategory("food", 100)),
                Arguments.of("{\"title\":\"шапка\",\"date\":\"2022.01.01\",\"sum\":60}", new MaxCategory("clothes", 110)),
                Arguments.of("{\"title\":\"куртка\",\"date\":\"2022.01.01\",\"sum\":70}", new MaxCategory("clothes", 180)),
                Arguments.of("{\"title\":\"сапоги\",\"date\":\"2022.01.01\",\"sum\":80}", new MaxCategory("clothes", 260)),
                Arguments.of("{\"title\":\"ведро\",\"date\":\"2022.01.01\",\"sum\":90}", new MaxCategory("clothes", 260)),
                Arguments.of("{\"title\":\"мыло\",\"date\":\"2022.01.01\",\"sum\":100}", new MaxCategory("clothes", 260)),
                Arguments.of("{\"title\":\"мочалка\",\"date\":\"2022.01.01\",\"sum\":110}", new MaxCategory("home", 300)),
                Arguments.of("{\"title\":\"швабра\",\"date\":\"2022.01.01\",\"sum\":120}", new MaxCategory("home", 420)),
                Arguments.of("{\"title\":\"акции\",\"date\":\"2022.01.01\",\"sum\":130}", new MaxCategory("home", 420)),
                Arguments.of("{\"title\":\"облигации\",\"date\":\"2022.01.01\",\"sum\":140}", new MaxCategory("home", 420)),
                Arguments.of("{\"title\":\"золото\",\"date\":\"2022.01.01\",\"sum\":151}", new MaxCategory("finance", 421)),
                Arguments.of("{\"title\":\"монетки\",\"date\":\"2022.01.01\",\"sum\":160}", new MaxCategory("finance", 581)),
                Arguments.of("{\"title\":\"текила\",\"date\":\"2022.01.01\",\"sum\":170}", new MaxCategory("finance", 581)),
                Arguments.of("{\"title\":\"пиво\",\"date\":\"2022.01.01\",\"sum\":180}", new MaxCategory("finance", 581)),
                Arguments.of("{\"title\":\"кола\",\"date\":\"2022.01.01\",\"sum\":190}", new MaxCategory("finance", 581)),
                Arguments.of("{\"title\":\"минералка\",\"date\":\"2022.01.01\",\"sum\":200}", new MaxCategory("drink", 740)),
                Arguments.of("{\"title\":\"хагиваги\",\"date\":\"2022.01.01\",\"sum\":210}", new MaxCategory("drink", 740)),
                Arguments.of("{\"title\":\"мячик\",\"date\":\"2022.01.01\",\"sum\":220}", new MaxCategory("drink", 740)),
                Arguments.of("{\"title\":\"кукла\",\"date\":\"2022.01.01\",\"sum\":230}", new MaxCategory("drink", 740)),
                Arguments.of("{\"title\":\"лего\",\"date\":\"2022.01.01\",\"sum\":240}", new MaxCategory("toy", 900)),
                Arguments.of("{\"title\":\"микрофон\",\"date\":\"2022.01.01\",\"sum\":250}", new MaxCategory("toy", 900)),
                Arguments.of("{\"title\":\"телефон\",\"date\":\"2022.01.01\",\"sum\":260}", new MaxCategory("toy", 900)),
                Arguments.of("{\"title\":\"подшипник\",\"date\":\"2022.01.01\",\"sum\":270}", new MaxCategory("toy", 900)),
                Arguments.of("{\"title\":\"удочка\",\"date\":\"2022.01.01\",\"sum\":280}", new MaxCategory("other", 1060)));
    }
}