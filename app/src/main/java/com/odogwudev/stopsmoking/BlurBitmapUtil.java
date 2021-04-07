package com.odogwudev.stopsmoking;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

class BlurBitmapUtil {

    private static final float BITMAP_SCALE=0.4f;

    /**
     * Specific methods of blurring pictures
     *
     * @param context Context object
     * @param image   Need a blurry picture
     * @return Blurred picture
     */
    public static Bitmap blurBitmap(Context context, Bitmap image, float blurRadius) {
        // Calculate the length and width of the reduced image
        int width=Math.round (image.getWidth () * BITMAP_SCALE);
        int height=Math.round (image.getHeight () * BITMAP_SCALE);

        // Use the reduced image as a pre-rendered image
        Bitmap inputBitmap=Bitmap.createScaledBitmap (image, width, height, false);
        // Create a rendered output image
        Bitmap outputBitmap=Bitmap.createBitmap (inputBitmap);

        // Create RenderScript Kernel Object
        RenderScript rs=RenderScript.create (context);
        // Create a Blur Effect RenderScript Tool Object
        ScriptIntrinsicBlur blurScript=ScriptIntrinsicBlur.create (rs, Element.U8_4 (rs));

        // Since RenderScript does not use a VM to allocate memory, you need to use the Allocation class to create and allocate memory space
        // When the Allocation object is created, the memory is actually empty.You need to fill the data with copyTo ().
        Allocation tmpIn=Allocation.createFromBitmap (rs, inputBitmap);
        Allocation tmpOut=Allocation.createFromBitmap (rs, outputBitmap);

        // Set the blur degree of the rendering, 25f is the maximum blur
        blurScript.setRadius (blurRadius);
        // Set the input memory of the blurScript object
        blurScript.setInput (tmpIn);
        // Save output data to output memory
        blurScript.forEach (tmpOut);

        // Fill data into Allocation
        tmpOut.copyTo (outputBitmap);

        return outputBitmap;
    }
}
