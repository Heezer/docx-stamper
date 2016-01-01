package org.wickedsource.docxstamper.replace.image;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.R;
import org.wickedsource.docxstamper.api.DocxStamperException;
import org.wickedsource.docxstamper.api.typeresolver.ITypeResolver;

import java.util.UUID;

/**
 * This ITypeResolver allows context objects to return objects of type Image. An expression that resolves to an Image
 * object will be replaced by an actual image in the resulting .docx document. The image will be put as an inline into
 * the surrounding paragraph of text.
 */
public class ImageResolver implements ITypeResolver {

    @Override
    public R resolve(WordprocessingMLPackage document, Object image) {
        try {
            // TODO: adding the same image twice will put the image twice into the docx-zip file. make the second
            //       addition of the same image a reference instead.
            Image img = (Image) image;
            return createRunWithImage(document, img.getImageBytes(), img.getFilename(), img.getAltText());
        } catch (Exception e) {
            throw new DocxStamperException("Error while adding image to document!", e);
        }
    }

    public static R createRunWithImage(WordprocessingMLPackage wordMLPackage, byte[] bytes, String filenameHint, String altText) throws Exception {
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

        int id1 = UUID.randomUUID().hashCode();
        int id2 = UUID.randomUUID().hashCode();
        if (filenameHint == null) {
            filenameHint = "";
        }
        if (altText == null) {
            altText = "";
        }

        Inline inline = imagePart.createImageInline(filenameHint, altText,
                id1, id2, false);

        // Now add the inline in w:p/w:r/w:drawing
        org.docx4j.wml.ObjectFactory factory = new org.docx4j.wml.ObjectFactory();
        org.docx4j.wml.R run = factory.createR();
        org.docx4j.wml.Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);

        return run;

    }

}