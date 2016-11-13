package net.softwarebude.gles_training;

import android.content.Context;
import android.graphics.Shader;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;

    private final Context context;
    private final FloatBuffer vertexData;

    private int program;

    private static final String U_COLOR = "u_Color";
    private int uColorLocation;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    public AirHockeyRenderer(Context context) {

        this.context = context;

        float[] tableVerticesWithTriangles = {

                // Border Triangle 1
                -0.8f, -0.8f,
                0.8f, 0.8f,
                -0.8f, 0.8f,

                // Border Triangle 2
                -0.8f, -0.8f,
                0.8f, -0.8f,
                0.8f, 0.8f,

                // Triangle 1
                -0.7f, -0.7f,
                0.7f, 0.7f,
                -0.7f, 0.7f,

                // Triangle 2
                -0.7f, -0.7f,
                0.7f, -0.7f,
                0.7f, 0.7f,

                // Middle line
                -0.7f, 0.0f,
                0.7f, 0.0f,

                // Mellets
                0.0f, -0.5f,
                0.0f, 0.5f,

                // Puck
                0.0f, 0.0f

        };

        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(tableVerticesWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.simple_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader= ShaderHelper.compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        if(LoggerConfig.ON) {
            ShaderHelper.validateProgram(program);
        }

        glUseProgram(program);

        uColorLocation = glGetUniformLocation(program, U_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);

        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);

        glUniform4f(uColorLocation, 0.3f, 0.17f, 0.13f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 6);

        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 6, 6);

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f,1.0f);
        glDrawArrays(GL_LINES, 12, 2);

        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f,1.0f);
        glDrawArrays(GL_POINTS, 14, 1);

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f,1.0f);
        glDrawArrays(GL_POINTS, 15, 1);

        glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f,1.0f);
        glDrawArrays(GL_POINTS, 16, 1);
    }
}
