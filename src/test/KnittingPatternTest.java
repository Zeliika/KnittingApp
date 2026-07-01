package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.knittingapp.dto.KnittingPattern;
import de.knittingapp.dto.Stitch;
import de.knittingapp.dto.StitchTypes;

import java.util.Arrays;
import java.util.Random;

/**
 * tests dto classes
 */
public class KnittingPatternTest {
    public static void main(String[] args) {
//        KnittingPattern testPattern = new KnittingPattern(10, 10, 1);
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                testPattern.getPattern()[i][j] = new Stitch(1, StitchTypes.NONE);
//                System.out.println(testPattern.getPattern()[i][j]);
//
//            }
//        }
//        System.out.println(testPattern);
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
////        String json = gson.toJson;
////        System.out.println(json);
////        Color test = Color.RED;
////        String colorString = String.format("#%08X", ((int)test.getRed() + (int)test.getGreen() + (int)test.getBlue()));
////        System.out.println(colorString);
////        Color reverse = Color.web(colorString);
//    }
//    public static String randomHexColor() {
//        Random random = new Random();
//        int rgb = random.nextInt(0x1000000);
//        return String.format("#%06X", rgb);


        int[][] test = {{1, 1}, {1, 1}};
        int[][] test2 = new int[test.length][test[0].length];
        for (int i = 0; i < test.length ; i++) {
            test2[i] = Arrays.copyOf(test[i],test[0].length);
        }
        test2[0][0] = 2;
        System.out.println(Arrays.deepToString(test));
        System.out.println(Arrays.deepToString(test2));


    }
}

