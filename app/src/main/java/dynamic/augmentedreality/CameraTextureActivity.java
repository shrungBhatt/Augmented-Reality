package dynamic.augmentedreality;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;

import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.TextureView;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL11;

import static android.opengl.GLES20.*;

public class CameraTextureActivity extends TextureView implements TextureView.SurfaceTextureListener {


    private Context mContext;
    private RenderThread mRenderThread;
    private Camera mCamera;

    private FloatBuffer mVertices;
    private final float[] mVerticesData = {
            0.0f, 0.5f, 0.0f, -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f
    };

    public CameraTextureActivity(Context context, Camera camera) {
        super(context);

        mContext = context;
        mCamera = camera;

        setSurfaceTextureListener(this);

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
/*
        try {
            mCamera.setDisplayOrientation(90);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            mCamera.setParameters(parameters);
            mCamera.setPreviewTexture(surfaceTexture);
            mCamera.startPreview();
        } catch (IOException ioe) {
            // Something bad happened
        }
*/

        mRenderThread = new RenderThread(surfaceTexture);
        mRenderThread.start();

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

//    private static class RenderThread extends Thread {
//        private static final String LOG_TAG = "GLTextureView";
//
//        static final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
//        static final int EGL_OPENGL_ES2_BIT = 4;
//
//        private boolean mFinished = false;
//
//        private final Resources mResources;
//        private final SurfaceTexture mSurface;
//
//        private EGL10 mEgl;
//        private EGLDisplay mEglDisplay;
//        private EGLConfig mEglConfig;
//        private EGLContext mEglContext;
//        private EGLSurface mEglSurface;
//        private GL11 mGL;
//
//        RenderThread(Resources resources, SurfaceTexture surface) {
//            mResources = resources;
//            mSurface = surface;
//        }
//
//        private static final String sSimpleVS =
//                "attribute vec4 position;\n" +
//                        "attribute vec2 texCoords;\n" +
//                        "varying vec2 outTexCoords;\n" +
//                        "\nvoid main(void) {\n" +
//                        " outTexCoords = texCoords;\n" +
//                        " gl_Position = position;\n" +
//                        "}\n\n";
//        private static final String sSimpleFS =
//                "precision mediump float;\n\n" +
//                        "varying vec2 outTexCoords;\n" +
//                        "uniform sampler2D texture;\n" +
//                        "\nvoid main(void) {\n" +
//                        " gl_FragColor = texture2D(texture, outTexCoords);\n" +
//                        "}\n\n";
//
//        private static final int FLOAT_SIZE_BYTES = 4;
//        private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;
//        private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
//        private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 3;
//        private final float[] mTriangleVerticesData = {
//                // X, Y, Z, U, V
//                -1.0f, -1.0f, 0.0f, 0.0f, 0.0f,
//                1.0f, -1.0f, 0.0f, 1.0f, 0.0f,
//                -1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
//                1.0f, 1.0f, 0.0f, 1.0f, 1.0f,
//        };
//
//        @Override
//        public void run() {
//            initGL();
//
//            FloatBuffer triangleVertices = ByteBuffer.allocateDirect(mTriangleVerticesData.length * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
//            triangleVertices.put(mTriangleVerticesData).position(0);
//
//            int texture = loadTexture(R.drawable.android);
//            int program = buildProgram(sSimpleVS, sSimpleFS);
//
//            int attribPosition = glGetAttribLocation(program, "position");
//            checkGlError();
//
//            int attribTexCoords = glGetAttribLocation(program, "texCoords");
//            checkGlError();
//
//            int uniformTexture = glGetUniformLocation(program, "texture");
//            checkGlError();
//
//            glBindTexture(GL_TEXTURE_2D, texture);
//            checkGlError();
//
//            glUseProgram(program);
//            checkGlError();
//
//            glEnableVertexAttribArray(attribPosition);
//            checkGlError();
//
//            glEnableVertexAttribArray(attribTexCoords);
//            checkGlError();
//
//            glUniform1i(uniformTexture, texture);
//            checkGlError();
//
//            glClearColor(1f, 1f, 1f, 1.0f);
//
//
//            while (!mFinished) {
//                checkCurrent();
//
//                glClearColor(0f, 0f, 0f, 1.0f);
//                checkGlError();
//
//                glClear(GL_COLOR_BUFFER_BIT);
//                checkGlError();
//
//                // drawQuad
//
//                triangleVertices.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);
//                glVertexAttribPointer(attribPosition, 3, GL_FLOAT, false,
//                        0, triangleVertices);
//
//                triangleVertices.position(TRIANGLE_VERTICES_DATA_UV_OFFSET);
//                glVertexAttribPointer(attribTexCoords, 2, GL_FLOAT, false,
//                        0, triangleVertices);
//
//                glDrawArrays(GL_TRIANGLES, 0, 4);
//
//                if (!mEgl.eglSwapBuffers(mEglDisplay, mEglSurface)) {
//                    throw new RuntimeException("Cannot swap buffers");
//                }
//                checkEglError();
//
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    // Ignore
//                }
//            }
//
//            finishGL();
//        }
//
//        private int loadTexture(int resource) {
//            int[] textures = new int[1];
//
//            glActiveTexture(GL_TEXTURE0);
//            glGenTextures(1, textures, 0);
//            checkGlError();
//
//            int texture = textures[0];
//            glBindTexture(GL_TEXTURE_2D, texture);
//            checkGlError();
//
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//
////            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
////            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
//
//            Bitmap bitmap = BitmapFactory.decodeResource(mResources, resource);
//
//            GLUtils.texImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bitmap, GL_UNSIGNED_BYTE, 0);
//            checkGlError();
//
//            bitmap.recycle();
//
//            return texture;
//        }
//
//        private int buildProgram(String vertex, String fragment) {
//            int vertexShader = buildShader(vertex, GL_VERTEX_SHADER);
//            if (vertexShader == 0) return 0;
//
//            int fragmentShader = buildShader(fragment, GL_FRAGMENT_SHADER);
//            if (fragmentShader == 0) return 0;
//
//            int program = glCreateProgram();
//            glAttachShader(program, vertexShader);
//            checkGlError();
//
//            glAttachShader(program, fragmentShader);
//            checkGlError();
//
//            glLinkProgram(program);
//            checkGlError();
//
//            int[] status = new int[1];
//            glGetProgramiv(program, GL_LINK_STATUS, status, 0);
//            if (status[0] != GL_TRUE) {
//                String error = glGetProgramInfoLog(program);
//                Log.d(LOG_TAG, "Error while linking program:\n" + error);
//                glDeleteShader(vertexShader);
//                glDeleteShader(fragmentShader);
//                glDeleteProgram(program);
//                return 0;
//            }
//
//            return program;
//        }
//
//        private int buildShader(String source, int type) {
//            int shader = glCreateShader(type);
//
//            glShaderSource(shader, source);
//            checkGlError();
//
//            glCompileShader(shader);
//            checkGlError();
//
//            int[] status = new int[1];
//            glGetShaderiv(shader, GL_COMPILE_STATUS, status, 0);
//            if (status[0] != GL_TRUE) {
//                String error = glGetShaderInfoLog(shader);
//                Log.d(LOG_TAG, "Error while compiling shader:\n" + error);
//                glDeleteShader(shader);
//                return 0;
//            }
//
//            return shader;
//        }
//
//        private void checkEglError() {
//            int error = mEgl.eglGetError();
//            if (error != EGL10.EGL_SUCCESS) {
//                Log.w(LOG_TAG, "EGL error = 0x" + Integer.toHexString(error));
//            }
//        }
//
//        private void checkGlError() {
//            int error = glGetError();
//            if (error != GL_NO_ERROR) {
//                Log.w(LOG_TAG, "GL error = 0x" + Integer.toHexString(error));
//            }
//        }
//
//        private void finishGL() {
//            mEgl.eglDestroyContext(mEglDisplay, mEglContext);
//            mEgl.eglDestroySurface(mEglDisplay, mEglSurface);
//        }
//
//        private void checkCurrent() {
//            if (!mEglContext.equals(mEgl.eglGetCurrentContext()) ||
//
//                    !mEglSurface.equals(mEgl.eglGetCurrentSurface(EGL10.EGL_DRAW))) {
//                if (!mEgl.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)) {
//                    throw new RuntimeException("eglMakeCurrent failed " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
//                }
//            }
//        }
//
//        private void initGL() {
//            mEgl = (EGL10) EGLContext.getEGL();
//
//            mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
//            if (mEglDisplay == EGL10.EGL_NO_DISPLAY) {
//                throw new RuntimeException("eglGetDisplay failed "
//                        + GLUtils.getEGLErrorString(mEgl.eglGetError()));
//            }
//
//            int[] version = new int[2];
//            if (!mEgl.eglInitialize(mEglDisplay, version)) {
//                throw new RuntimeException("eglInitialize failed " +
//                        GLUtils.getEGLErrorString(mEgl.eglGetError()));
//            }
//
//            mEglConfig = chooseEglConfig();
//            if (mEglConfig == null) {
//                throw new RuntimeException("eglConfig not initialized");
//            }
//
//            mEglContext = createContext(mEgl, mEglDisplay, mEglConfig);
//
//            mEglSurface = mEgl.eglCreateWindowSurface(mEglDisplay, mEglConfig, mSurface, null);
//
//            if (mEglSurface == null || mEglSurface == EGL10.EGL_NO_SURFACE) {
//                int error = mEgl.eglGetError();
//                if (error == EGL10.EGL_BAD_NATIVE_WINDOW) {
//                    Log.e(LOG_TAG, "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
//                    return;
//                }
//                throw new RuntimeException("createWindowSurface failed "
//                        + GLUtils.getEGLErrorString(error));
//            }
//
//            if (!mEgl.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)) {
//                throw new RuntimeException("eglMakeCurrent failed "
//                        + GLUtils.getEGLErrorString(mEgl.eglGetError()));
//            }
//
//            mGL = mEglContext.getGL();
//        }
//
//
//        EGLContext createContext(EGL10 egl, EGLDisplay eglDisplay, EGLConfig eglConfig) {
//            int[] attrib_list = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE};
//            return egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list);
//        }
//
//        private EGLConfig chooseEglConfig() {
//            int[] configsCount = new int[1];
//            EGLConfig[] configs = new EGLConfig[1];
//            int[] configSpec = getConfig();
//            if (!mEgl.eglChooseConfig(mEglDisplay, configSpec, configs, 1, configsCount)) {
//                throw new IllegalArgumentException("eglChooseConfig failed " +
//                        GLUtils.getEGLErrorString(mEgl.eglGetError()));
//            } else if (configsCount[0] > 0) {
//                return configs[0];
//            }
//            return null;
//        }
//
//        private int[] getConfig() {
//            return new int[]{
//                    EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
//                    EGL10.EGL_RED_SIZE, 8,
//                    EGL10.EGL_GREEN_SIZE, 8,
//                    EGL10.EGL_BLUE_SIZE, 8,
//                    EGL10.EGL_ALPHA_SIZE, 8,
//                    EGL10.EGL_DEPTH_SIZE, 0,
//                    EGL10.EGL_STENCIL_SIZE, 0,
//                    EGL10.EGL_NONE
//            };
//        }
//
//        void finish() {
//            mFinished = true;
//        }
//    }

    private class RenderThread extends Thread {
        private static final int EGL_OPENGL_ES2_BIT = 4;
        private static final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
        private static final String TAG = "RenderThread";
        private SurfaceTexture mSurface;
        private EGLDisplay mEglDisplay;
        private EGLSurface mEglSurface;
        private EGLContext mEglContext;
        private int mProgram;
        private EGL10 mEgl;
        private GL11 mGl;

        public RenderThread(SurfaceTexture surface) {
            mSurface = surface;

            mVertices = ByteBuffer.allocateDirect(mVerticesData.length * 4)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            mVertices.put(mVerticesData).position(0);
        }

        @Override
        public void run() {
            initGL();

            int attribPosition = GLES20.glGetAttribLocation(mProgram,
                    "position");
            checkGlError();

            GLES20.glEnableVertexAttribArray(attribPosition);
            checkGlError();

            GLES20.glUseProgram(mProgram);
            checkGlError();

            while (true) {
                checkCurrent();

                mVertices.position(0);
                GLES20.glVertexAttribPointer(attribPosition, 3,
                        GLES20.GL_FLOAT, false, 0, mVertices);
                checkGlError();

                GLES20.glClearColor(1.0f, 1.0f, 0, 0);
                checkGlError();

                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
                checkGlError();

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
                Log.d(TAG, "draw!!");
                checkGlError();

                if (!mEgl.eglSwapBuffers(mEglDisplay, mEglSurface)) {
                    Log.e(TAG, "cannot swap buffers!");
                }
                checkEglError();

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // Ignore
                }
            }
        }

        private void checkCurrent() {
            if (!mEglContext.equals(mEgl.eglGetCurrentContext())
                    || !mEglSurface.equals(mEgl
                    .eglGetCurrentSurface(EGL10.EGL_DRAW))) {
                checkEglError();
                if (!mEgl.eglMakeCurrent(mEglDisplay, mEglSurface,
                        mEglSurface, mEglContext)) {
                    throw new RuntimeException(
                            "eglMakeCurrent failed "
                                    + GLUtils.getEGLErrorString(mEgl
                                    .eglGetError()));
                }
                checkEglError();
            }
        }

        private void checkEglError() {
            final int error = mEgl.eglGetError();
            if (error != EGL10.EGL_SUCCESS) {
                Log.e(TAG, "EGL error = 0x" + Integer.toHexString(error));
            }
        }

        private void checkGlError() {
            final int error = mGl.glGetError();
            if (error != GL11.GL_NO_ERROR) {
                Log.e(TAG, "GL error = 0x" + Integer.toHexString(error));
            }
        }

        private int buildProgram(String vertexSource, String fragmentSource) {
            final int vertexShader = buildShader(GLES20.GL_VERTEX_SHADER,
                    vertexSource);
            if (vertexShader == 0) {
                return 0;
            }

            final int fragmentShader = buildShader(
                    GLES20.GL_FRAGMENT_SHADER, fragmentSource);
            if (fragmentShader == 0) {
                return 0;
            }

            final int program = GLES20.glCreateProgram();
            if (program == 0) {
                return 0;
            }

            GLES20.glAttachShader(program, vertexShader);
            checkGlError();

            GLES20.glAttachShader(program, fragmentShader);
            checkGlError();

            GLES20.glLinkProgram(program);
            checkGlError();

            int[] status = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status,
                    0);
            checkGlError();
            if (status[0] == 0) {
                Log.e(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                checkGlError();
            }

            return program;
        }

        private int buildShader(int type, String shaderSource) {
            final int shader = GLES20.glCreateShader(type);
            if (shader == 0) {
                return 0;
            }

            GLES20.glShaderSource(shader, shaderSource);
            checkGlError();
            GLES20.glCompileShader(shader);
            checkGlError();

            int[] status = new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status,
                    0);
            if (status[0] == 0) {
                Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                return 0;
            }

            return shader;
        }

        private void initGL() {
            final String vertexShaderSource = "attribute vec4 position;\n"
                    +
                    "void main () {\n" +
                    "   gl_Position = position;\n" +
                    "}";

            final String fragmentShaderSource = "precision mediump float;\n"
                    +
                    "void main () {\n" +
                    "   gl_FragColor = vec4(1.0, 0.0, 0.0, 0.0);\n" +
                    "}";

            mEgl = (EGL10) EGLContext.getEGL();

            mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
            if (mEglDisplay == EGL10.EGL_NO_DISPLAY) {
                throw new RuntimeException("eglGetDisplay failed "
                        + GLUtils.getEGLErrorString(mEgl.eglGetError()));
            }

            int[] version = new int[2];
            if (!mEgl.eglInitialize(mEglDisplay, version)) {
                throw new RuntimeException("eglInitialize failed "
                        + GLUtils.getEGLErrorString(mEgl.eglGetError()));
            }

            int[] configsCount = new int[1];
            EGLConfig[] configs = new EGLConfig[1];
            int[] configSpec = {
                    EGL10.EGL_RENDERABLE_TYPE,
                    EGL_OPENGL_ES2_BIT,
                    EGL10.EGL_RED_SIZE, 8,
                    EGL10.EGL_GREEN_SIZE, 8,
                    EGL10.EGL_BLUE_SIZE, 8,
                    EGL10.EGL_ALPHA_SIZE, 8,
                    EGL10.EGL_DEPTH_SIZE, 0,
                    EGL10.EGL_STENCIL_SIZE, 0,
                    EGL10.EGL_NONE
            };

            EGLConfig eglConfig = null;
            if (!mEgl.eglChooseConfig(mEglDisplay, configSpec, configs, 1,
                    configsCount)) {
                throw new IllegalArgumentException(
                        "eglChooseConfig failed "
                                + GLUtils.getEGLErrorString(mEgl
                                .eglGetError()));
            } else if (configsCount[0] > 0) {
                eglConfig = configs[0];
            }
            if (eglConfig == null) {
                throw new RuntimeException("eglConfig not initialized");
            }

            int[] attrib_list = {
                    EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE
            };
            mEglContext = mEgl.eglCreateContext(mEglDisplay,
                    eglConfig, EGL10.EGL_NO_CONTEXT, attrib_list);
            checkEglError();
            mEglSurface = mEgl.eglCreateWindowSurface(
                    mEglDisplay, eglConfig, mSurface, null);
            checkEglError();
            if (mEglSurface == null || mEglSurface == EGL10.EGL_NO_SURFACE) {
                int error = mEgl.eglGetError();
                if (error == EGL10.EGL_BAD_NATIVE_WINDOW) {
                    Log.e(TAG,
                            "eglCreateWindowSurface returned EGL10.EGL_BAD_NATIVE_WINDOW");
                    return;
                }
                throw new RuntimeException(
                        "eglCreateWindowSurface failed "
                                + GLUtils.getEGLErrorString(error));
            }

            if (!mEgl.eglMakeCurrent(mEglDisplay, mEglSurface,
                    mEglSurface, mEglContext)) {
                throw new RuntimeException("eglMakeCurrent failed "
                        + GLUtils.getEGLErrorString(mEgl.eglGetError()));
            }
            checkEglError();

            mGl = (GL11) mEglContext.getGL();
            checkEglError();

            mProgram = buildProgram(vertexShaderSource,
                    fragmentShaderSource);
        }
    }
}
