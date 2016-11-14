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
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4;

    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final Context context;
    private final FloatBuffer vertexData;

    private int program;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    private static final String A_COLOR = "a_Color";
    private int aColorLocation;

    public AirHockeyRenderer(Context context) {

        this.context = context;

        float[] tableVerticesWithTriangles = {
                // x, y, r, g, b

                // Border Trianglefan
                0.0f, 0.0f, 0.3f, 0.17f, 0.13f,
                -0.8f, -0.8f, 0.3f, 0.17f, 0.13f,
                0.8f, -0.8f, 0.3f, 0.17f, 0.13f,
                0.8f, 0.8f, 0.3f, 0.17f, 0.13f,
                -0.8f, 0.8f, 0.3f, 0.17f, 0.13f,
                -0.8f, -0.8f, 0.3f, 0.17f, 0.13f,

                // Trianglefan
                0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
                -0.7f, -0.7f, 0.7f, 0.7f, 0.7f,
                0.0f, -0.7f, 0.7f, 0.7f, 0.7f,
                0.7f, -0.7f, 0.7f, 0.7f, 0.7f,
                0.7f, 0.7f, 0.7f, 0.7f, 0.7f,
                0.0f, 0.7f, 0.7f, 0.7f, 0.7f,
                -0.7f, 0.7f, 0.7f, 0.7f, 0.7f,
                -0.7f, -0.7f, 0.7f, 0.7f, 0.7f,

                // Middle line
                -0.7f, 0.0f, 1.0f, 0.0f, 0.0f,
                0.7f, 0.0f, 0.0f, 0.0f, 1.0f,

                // Mellets
                0.0f, -0.5f, 0.0f, 0.0f, 1.0f,
                0.0f, 0.5f, 1.0f, 0.0f, 0.0f,

                // Puck
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f

        };

        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * STRIDE)
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

        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);

        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
        glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);

        // Table border
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        // Field
        glDrawArrays(GL_TRIANGLE_FAN, 6, 8);

        // Middle line
        glDrawArrays(GL_LINES, 14, 2);

        // Mellets
        glDrawArrays(GL_POINTS, 16, 1);
        glDrawArrays(GL_POINTS, 17, 1);

        // Puck
        glDrawArrays(GL_POINTS, 18, 1);
    }
}
