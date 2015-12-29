package org.wickedsource.docxstamper;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.junit.Assert;
import org.junit.Test;
import org.wickedsource.docxstamper.context.ImageContext;
import org.wickedsource.docxstamper.docx4j.AbstractDocx4jTest;
import org.wickedsource.docxstamper.docx4j.replace.image.Image;

import javax.xml.bind.JAXBElement;
import java.io.IOException;
import java.io.InputStream;

public class ImageReplacementInGlobalParagraphsTest extends AbstractDocx4jTest {

    @Test
    public void test() throws Docx4JException, IOException {
        Image monalisa = new Image(getClass().getResourceAsStream("monalisa.jpg"));
        ImageContext context = new ImageContext();
        context.setMonalisa(monalisa);

        InputStream template = getClass().getResourceAsStream("ImageReplacementInGlobalParagraphsTest.docx");
        WordprocessingMLPackage document = stampAndLoad(template, context);

        Assert.assertTrue(((JAXBElement) ((R) ((P) document.getMainDocumentPart().getContent().get(2)).getContent().get(1)).getContent().get(0)).getValue() instanceof Drawing);
        Assert.assertTrue(((JAXBElement) ((R) ((P) document.getMainDocumentPart().getContent().get(3)).getContent().get(1)).getContent().get(0)).getValue() instanceof Drawing);

    }


}
