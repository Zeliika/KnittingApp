package de.knittingapp.backend;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import de.knittingapp.dto.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ArrayList;

/**
 * <pre>
 *     custom TypeAdapter to convert KnittingProject object to Json String and Json String to KnittingProjectObject
 * </pre>
 */
public class KnittingProjectTypeAdapter extends TypeAdapter<KnittingProject> {

    @Override
    public void write(JsonWriter jsonWriter, KnittingProject knittingProject) throws IOException {
        if (knittingProject == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.beginObject();

        jsonWriter.name("title").value(knittingProject.getTitle());
        jsonWriter.name("description").value(knittingProject.getDescription());

        jsonWriter.name("instructionsList").beginArray();
        for(KnittingInstruction knittingInstruction : knittingProject.getKnittingInstructions().values()){
            jsonWriter.beginObject();
            jsonWriter.name("name").value(knittingInstruction.getName());
            jsonWriter.name("writtenInstructions").value(knittingInstruction.getInstructions());

            if (knittingInstruction instanceof KnittingPattern){
                KnittingPattern pattern = (KnittingPattern)knittingInstruction;
                jsonWriter.name("type").value("pattern");
                jsonWriter.name("ratio").value(pattern.getAspectRatioStitch());
                jsonWriter.name("backgroundColor").value((pattern.getBackgroundColor().toString().replace("0x", "#")));

                jsonWriter.name("pattern").beginArray();
                for (Stitch[] row : ((KnittingPattern)knittingInstruction).getPattern()){
                    jsonWriter.beginArray();
                    for (Stitch stitch : row){
                        jsonWriter.beginObject();
                        jsonWriter.name("colorIndex").value(stitch.getColorIndex());
                        jsonWriter.name("stitchType").value(stitch.getStitchType().ordinal());
                        jsonWriter.endObject();
                    }
                    jsonWriter.endArray();
                }
                jsonWriter.endArray();

                jsonWriter.name("colorPallet").beginArray();
                for (Color color: ((KnittingPattern) knittingInstruction).getColorPallet()){
                    if(color != null){
                        jsonWriter.value(color.toString().replace("0x", "#"));
                    } else {
                        jsonWriter.nullValue();
                    }
                }
                jsonWriter.endArray();

            } else {
                jsonWriter.name("type").value("text");
            }
            jsonWriter.endObject();
        }
        jsonWriter.endArray();
        jsonWriter.endObject();
    }

    @Override
    public KnittingProject read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        KnittingProject project = new KnittingProject();

        jsonReader.beginObject();
        while(jsonReader.hasNext()){
            String fieldName = jsonReader.nextName();

            switch (fieldName){
                case "title":
                    project.setTitle(jsonReader.nextString());
                    break;
                case "description":
                    project.setDescription(jsonReader.nextString());
                    break;
                case "instructionsList":
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()){
                        String type = null;
                        String name = null;
                        String instructions = null;
                        double ratio = 0;
                        Stitch[][] pattern = null;
                        Color[] colorPallet = null;
                        Color backgroundColor = null;
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()){
                            String instructionField = jsonReader.nextName();
                            switch (instructionField){
                                case "name":
                                    name = jsonReader.nextString();
                                    break;
                                case "writtenInstructions":
                                    instructions = jsonReader.nextString();
                                    break;
                                case "type":
                                    type = jsonReader.nextString();
                                    break;
                                case "ratio":
                                    ratio = jsonReader.nextDouble();
                                    break;
                                case "pattern":
                                    pattern = readStitchArray(jsonReader);
                                    break;
                                case "colorPallet":
                                    colorPallet = readColorPallet(jsonReader);
                                    break;
                                case "backgroundColor":
                                    backgroundColor = Color.web(jsonReader.nextString());
                            }
                        }
                        jsonReader.endObject();

                        KnittingInstruction knittingInstruction;
                        if ("pattern".equals(type)){
                            knittingInstruction = new KnittingPattern(name,pattern,ratio,instructions, colorPallet, backgroundColor);
                        } else {
                            knittingInstruction = new KnittingInstruction(name,instructions);
                        }

                        project.addKnittingInstruction(knittingInstruction);

                    }
                    jsonReader.endArray();
            }
        }
        jsonReader.endObject();
        return project;
    }

    private Color[] readColorPallet(JsonReader jsonReader) throws IOException{
        ArrayList<Color> colors = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()){
            if(jsonReader.peek() == JsonToken.NULL){
                jsonReader.nextNull();
                colors.add(null);
            } else {
                colors.add(Color.web(jsonReader.nextString()));
            }
        }
        jsonReader.endArray();

        return colors.toArray(new Color[0]);
    }

    private Stitch[][] readStitchArray(JsonReader jsonReader) throws IOException {
        ArrayList<Stitch[]> rows = new ArrayList<>();

        jsonReader.beginArray();
        while (jsonReader.hasNext()){
            ArrayList<Stitch> stitchesInRow = new ArrayList<>();
            jsonReader.beginArray();
            while (jsonReader.hasNext()){
                jsonReader.beginObject();
                int colorIndex = 1;
                StitchTypes stitchType = StitchTypes.NONE;
                while (jsonReader.hasNext()){
                    String stitchField = jsonReader.nextName();
                    switch (stitchField){
                        case "colorIndex" -> colorIndex = jsonReader.nextInt();
                        case "stitchType" -> stitchType = StitchTypes.values()[jsonReader.nextInt()];
                    }
                }
                jsonReader.endObject();
                stitchesInRow.add(new Stitch(colorIndex, stitchType));
            }
            jsonReader.endArray();
            rows.add(stitchesInRow.toArray(new Stitch[0]));
        }
        jsonReader.endArray();

        return rows.toArray(new Stitch[0][]);

    }
}
