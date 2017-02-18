/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.samples.vision.ocrreader;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.google.android.gms.samples.vision.ocrreader.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.text.TextBlock;

/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
public class OcrGraphic extends GraphicOverlay.Graphic {

    private int mId;
    boolean read;
    private static final int TEXT_COLOR = Color.WHITE;

    private static Paint sRectPaint;
    private static Paint sTextPaint;
    private final TextBlock mText;
    public static String ocrText ;


    OcrGraphic(GraphicOverlay overlay, TextBlock text) {
        super(overlay);

        mText = text;

        if (sRectPaint == null) {
            sRectPaint = new Paint();
            sRectPaint.setColor(TEXT_COLOR);
            sRectPaint.setStyle(Paint.Style.STROKE);
            sRectPaint.setStrokeWidth(4.0f);
        }

        if (sTextPaint == null) {
            sTextPaint = new Paint();
            sTextPaint.setColor(TEXT_COLOR);
            sTextPaint.setTextSize(54.0f);
        }
        // Redraw the overlay, as this graphic has been added.
        postInvalidate();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public TextBlock getTextBlock() {

        return mText;
    }

    /**
     * Checks whether a point is within the bounding box of this graphic.
     * The provided point should be relative to this graphic's containing overlay.
     * @param x An x parameter in the relative context of the canvas.
     * @param y A y parameter in the relative context of the canvas.
     * @return True if the provided point is contained within this graphic's bounding box.
     */
    public boolean contains(float x, float y) {
        TextBlock text = mText;
        if (text == null) {
            return false;
        }
        RectF rect = new RectF(text.getBoundingBox());
        rect.left = translateX(rect.left);
        rect.top = translateY(rect.top);
        rect.right = translateX(rect.right);
        rect.bottom = translateY(rect.bottom);
        return (rect.left < x && rect.right > x && rect.top < y && rect.bottom > y);
    }

    public String getText(){
        return  ocrText;
    }
    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        TextBlock text = mText;

        if (text == null ) {
            return;
        }
        if(OcrCaptureActivity.meter1) {
            String myText = text.getValue();
            try {
                myText = myText.split("m")[0];
            } catch (Exception e) {

            }
            try {   // Draws the bounding box around the TextBlock.
                ocrText = "";
                myText = myText.split("CE")[1];

                myText = myText.split("\n")[0];

                myText = myText.replace(" ", "");

                if (myText == "") {
                    return;
                }
                for (int i = 0; i < myText.length(); i++) {
                    switch (myText.charAt(i)) {
                        case 'D':
                        case 'o':
                        case 'C':
                        case 'O':
                        case 'a':
                            ocrText += 0;
                            break;
                        case 'A':
                            ocrText += 4;
                            break;
                        case 'G':
                            ocrText += 0;
                            break;
                        case 'B':
                            ocrText += 8;
                            break;
                        case 'S':
                        case 's':
                            ocrText += 5;
                            break;
                        case  '.':
                        case ' ':
                            ocrText+="" ;break;
                        case 'm':ocrText+="";break;
                        default:
                            ocrText += myText.charAt(i);
                    }
                    Log.v("hihihi", i + "myText");
                }

                RectF rect = new RectF(text.getBoundingBox());
                rect.left = translateX(rect.left);
                rect.top = translateY(rect.top);
                rect.right = translateX(rect.right);
                rect.bottom = translateY(rect.bottom);
                canvas.drawRect(rect, sRectPaint);
                canvas.drawText(ocrText, rect.left, rect.bottom, sTextPaint);
                OcrCaptureActivity.backPressed = 0;
                MainActivity.ocrDetect++;
                MainActivity.ocrFinal="";
                MainActivity.ocrFinal = ocrText;
            } catch (Exception e) {

            }
        }else if(OcrCaptureActivity.meter2){
                String myText = text.getValue();

                try {
                    myText = myText.split("CE")[1];
                    myText = myText.split("cE")[1];

                } catch (Exception e) {

                }
                try {   // Draws the bounding box around the TextBlock.
                    ocrText = "";
                    myText = myText.split("\n")[0];
                    myText = myText.replace(" ", "");
                    myText = myText.split("m")[0];
                    if (!(myText.equalsIgnoreCase("DSSD") || myText.equalsIgnoreCase("p01") ||
                            myText.equalsIgnoreCase("ulp") || myText.contains("Q") || myText.contains("r")
                            || myText.contains("2014"))) {
                        if (myText == "") {
                            return;
                        }
                        for (int i = 0; i < myText.length(); i++) {
                            switch (myText.charAt(i)) {
                                case 'I':
                                case 'T':
                                case 'i':
                                    ocrText += 1;
                                    break;
                                case 'D':
                                case 'o':
                                case 'C':
                                case 'O':
                                    ocrText += 0;
                                    break;
                                case 'A':
                                    ocrText += 4;
                                    break;
                                case 'G':
                                    ocrText += 0;
                                    break;
                                case 'B':
                                    ocrText += 8;
                                    break;
                                case 'S':
                                case 's':
                                    ocrText += 5;
                                    break;
                                case '.':
                                case ' ':
                                case 'm':
                                    ocrText += "";
                                    break;
                                default:
                                    ocrText += myText.charAt(i);
                            }
                            Log.v("hihihi", i + "myText");
                        }
                        try {
                            Integer.parseInt(ocrText);
                            RectF rect = new RectF(text.getBoundingBox());
                            rect.left = translateX(rect.left);
                            rect.top = translateY(rect.top);
                            rect.right = translateX(rect.right);
                            rect.bottom = translateY(rect.bottom);
                            canvas.drawRect(rect, sRectPaint);
                            canvas.drawText(ocrText, rect.left, rect.bottom, sTextPaint);
                            OcrCaptureActivity.backPressed = 0;
                            MainActivity.ocrDetect++;
                            MainActivity.ocrFinal = "";
                            MainActivity.ocrFinal = ocrText;
                        }catch (Exception e){

                        }
                    }

                } catch (Exception e) {

                }

        }
        // Break the text into multiple lines and draw each one according to its own bounding box.
      /*  List<? extends Text> textComponents = text.getComponents();
        for(Text currentText : textComponents) {
            float left = translateX(currentText.getBoundingBox().left);
            float bottom = translateY(currentText.getBoundingBox().bottom);
            canvas.drawText(currentText.getValue().split("CE")[1], left, bottom, sTextPaint);
        }*/
    }

}
