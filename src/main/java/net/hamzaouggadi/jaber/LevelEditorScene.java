package net.hamzaouggadi.jaber;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private String vertexShaderSrc = """
            #version 330 core
            layout(location=0) in vec3 aPos;
            layout(location=1) in vec4 aColor;

            out vec4 fColor;

            void main() {
                fColor = aColor;
                gl_Position = vec4(aPos, 1.0);
            }""";
    private String fragmentShaderSrc = """
            #version 330 core

            in vec4 fColor;

            out vec4 color;

            void main() {
                color = fColor;
            }""";

    private int vertexId, fragmentId, shaderProgram;
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
        // ***********************
        // * Compile and Link the shaders
        // ***********************

        // Load and compile the vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source
        glShaderSource(vertexId, vertexShaderSrc);
        glCompileShader(vertexId);

        // Check for errors in compilation
        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        // If compilation failed success has 0 as a value
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR : 'defaultShader.glsl'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexId, len));
            assert false : "";
        }

        // Load and compile the vertex shader
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source
        glShaderSource(fragmentId, fragmentShaderSrc);
        glCompileShader(fragmentId);

        // Check for errors in compilation
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        // If compilation failed success has 0 as a value
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR : 'defaultShader.glsl'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentId, len));
            assert false : "";
        }

        // Link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexId);
        glAttachShader(shaderProgram, fragmentId);
        glLinkProgram(shaderProgram);

        // Check for Linking errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR : 'defaultShader.glsl'\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }


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
        // Bind Shader program
        glUseProgram(shaderProgram);
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

        glUseProgram(0);

    }


}
