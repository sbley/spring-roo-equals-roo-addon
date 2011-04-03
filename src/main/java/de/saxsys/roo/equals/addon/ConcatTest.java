package de.saxsys.roo.equals.addon;

import org.eclipse.xtext.xtend2.lib.StringConcatenation;

public class ConcatTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        StringConcatenation sc1 = new StringConcatenation();
        sc1.append("Foo{");
        sc1.newLine();
        sc1.append("\t");
        sc1.append("Bar");
        sc1.newLine();
        sc1.append("}");
        sc1.newLine();

        System.out.println(sc1);

        StringConcatenation sc2 = new StringConcatenation();
        sc2.append("Start{");
        sc2.newLine();
        sc2.append("\t");
        sc2.append(sc1, "\t");
        sc2.newLineIfNotEmpty();
        sc2.append("}");
        sc2.newLine();

        System.out.println(sc2);

    }

}
