package com.example.asimikram.batterytest_new;

/**
 * Created by Asim Ikram on 30-Apr-17.
 */

import java.io.BufferedReader;

//import java.io.File;
import java.io.FileReader;
//import java.io.IOException;


//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;



import java.util.Random;

//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.InstanceTools;
//import net.sf.javaml.tools.data.FileHandler;
//import net.sf.javaml.*;

public class classifier {
    /**
     * Shows the default usage of the KNN algorithm.
     */

    String check = null;
    public classifier(){}

    public String classification() throws Exception
    {
        Dataset data = new DefaultDataset();
        Dataset dataForClassification = new DefaultDataset();
        Random ran = new Random();
//
//        FileReader freader = new FileReader("Apps.csv");
//        BufferedReader reader = new BufferedReader(freader);
//        String line = null;
//        int i = 0;
//        while ((line = reader.readLine()) != null) {
//            if (i == 0) {
//                i++;
//            } else {
//                String[] str_data = line.split(",");
//                System.out.println("Game: " + str_data[0] + "\t" + "Social Media: " + str_data[1] + "\t" + "Multimedia: " + str_data[2]);
//            }
//        }

        // here i create some training dataset
        for (int j = 0; j < 500; j++) {                 //Create 500 rows, 30 columns each
            Instance tmpInstance = InstanceTools.randomInstance(30); // create record of 3 dimensions
            int count_social = 0,count_game = 0, count_multi = 0;
            for (int c = 0; c < 30; c++) {
                int x = ran.nextInt(2);
                double x_con = x;   //tmpInstance only accepts double
                tmpInstance.put(c, x_con);
                if(c<10 && x==1)        //Determine to set class label to be game, social, or multi
                {
                    count_game++;
                }
                else if (c>=10 && c<20 && x==1)
                {
                    count_social++;
                }
                else if( c>=20 && x==1)
                {
                    count_multi++;
                }
            }

            if (count_game > count_social && count_game > count_multi) {
                tmpInstance.setClassValue(0);
            } else if (count_social > count_game && count_social > count_multi) {
                tmpInstance.setClassValue(1);
            } else {
                tmpInstance.setClassValue(2);
            }
            data.add(tmpInstance);
        }

        //System.out.println("\n"+data.size()+"\n");

        // create testing dataset
        RunningApps obj = new RunningApps();
        String[] user_data = obj.get_data();
        for (int j = 0; j < 1; j++) {
            Instance tmpInstance = InstanceTools.randomInstance(30);

            for (int c = 0; c < 30; c++) {
                if (user_data[c] != null) {
                    double x_con = 1.0;
                    tmpInstance.put(c, x_con);
                } else {
                    double x_con = 0.0;
                    tmpInstance.put(c, x_con);
                }
            }
            dataForClassification.add(tmpInstance);
        }

        Classifier knn = new KNearestNeighbors(7);
        knn.buildClassifier(data);

        //float correct = 0, wrong = 0;
		/* Classify all instances and check with the correct class values */

        for (Instance inst : dataForClassification) {
            Object predictedClassValue = knn.classify(inst);
            Object realClassValue = inst.classValue();

            check = predictedClassValue.toString();

//            if (predictedClassValue.equals(realClassValue))
//                correct++;
//            else
//                wrong++;
            if (check.equals("0"))
            {
                return "Games";
            }
             else if (check.equals("1"))
            {
                return "Social";
            }
            else
            {
                return "Multi";
            }
        }
//        System.out.println("\n\nCorrect predictions  " + correct);
//        System.out.println("Wrong predictions " + wrong);
//        float acc = (correct / (correct + wrong)) * 100;
//        System.out.println("Accuracy = " + acc);
        //System.out.println("\n\nDataset  " + data);
        return "Multi";
    }
    public static void main(String[] args)throws Exception
    {
        classifier obj = new classifier();
        obj.classification();
    }
}
