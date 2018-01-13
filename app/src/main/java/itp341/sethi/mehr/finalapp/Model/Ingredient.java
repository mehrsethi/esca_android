package itp341.sethi.mehr.finalapp.Model;

/**
 * Created by mehrsethi on 11/29/17.
 */

//contains the translated and original version of the ingredient's name
public class Ingredient {

    String original;
    String translate;

    public Ingredient() {
        original = "";
        translate = "";
    }

    public String getOriginal() {
        return original;
    }

    public String getTranslate() {
        return translate;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }
}
