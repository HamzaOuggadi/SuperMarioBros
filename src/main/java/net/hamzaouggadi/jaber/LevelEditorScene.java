package net.hamzaouggadi.jaber;

import net.hamzaouggadi.jaber.renderer.Shader;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {
    private int vertexId, fragmentId, shaderProgram;
    private Shader defaultShader;

    private float[] vertexArray = {
            // Position                 // Color
            -0.5f, 0.5f, 0.0f,          1.0f, 0.0f, 0.0f, 1.0f, // Top Left
            0.5f, 0.5f, 0.0f,           0.0f, 1.0f, 0.0f, 1.0f, // Top Right
            -0.5f, -0.5f, 0.0f,         0.0f, 0.0f, 1.0f, 1.0f, // Bottom Left
            0.5f, -0.5f, 0.0f,          1.0f, 1.0f, 1.0f, 1.0f  // Bottom Right
    };
    private int[] elementArray = {
            3, 1, 0,    // Top right triangle
            3, 0, 2     // Bottom left triangle
    };

    private int vaoId, vboId, eboId;

    public LevelEditorScene() {

    }

    @Override
    public void init() {

        defaultShader = new Shader("assets/shaders/defaultShader.glsl");
        defaultShader.compile();

        // ***********************
        // * Generate VAO, VBO and EBO buffer Objects, and send to GPU
        // ***********************
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create Float Buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO and upload the vertex buffer
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attributes pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize*floatSizeBytes);
        glEnableVertexAttribArray(1);


    }

    @Override
    public void update(float dt) {
        defaultShader.use();
        // Bind the VAO that we're using
        glBindVertexArray(vaoId);

        //Enable the Vertex attribute pointer
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind Everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

    }


}
