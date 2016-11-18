package net.softwarebude.gles_training.objects;

import static android.opengl.GLES20.*;
import net.softwarebude.gles_training.data.VertexArray;
import net.softwarebude.gles_training.programs.ColorShaderProgram;

public class Mallet {
    private static final int BYTES_PER_FLOAT = 4;

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private static final float[] VERTEX_DATA = {
            // Coordinate order: X, Y, R, G, B
            0f,     -0.4f,  0f,     0f,     1f,
            0f,     0.4f,   1f,     0f,     0f
    };

    private final VertexArray vertexArray;

    public Mallet() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(ColorShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                colorProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE
        );
    }

    public void draw() {
        glDrawArrays(GL_POINTS, 0, 2);
    }
}

