package net.softwarebude.gles_training.programs;

import android.content.Context;
import net.softwarebude.gles_training.R;

import static android.opengl.GLES20.*;

public class TextureShaderProgram extends ShaderProgram {
    // uniform locations
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    // attribute location
    private final int aPositionLocation;
    private final int aTextureCoordinateLocation;

    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);

        // retrieve uniform locations for the shader program
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);

        // retrieve attribute locations for the shader program
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinateLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] matrix, int textureId) {
        // pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        // set the active texture unit to texture unit 0
        glActiveTexture(GL_TEXTURE0);

        // bind the texture to this unit
        glBindTexture(GL_TEXTURE_2D, textureId);

        // tell the texture uniform sampler to use this texture in the shader by telling it to read from texture unit 0
        glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinateLocation;
    }
}
