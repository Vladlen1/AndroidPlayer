#include <jni.h>
#include <string>

extern "C"
jstring
Java_com_example_vladbirukov_player_ActiviteTwo_cackduration(JNIEnv *env, jobject instance,
                                                             jdoubleArray mas_) {
    jdouble *mas = env->GetDoubleArrayElements(mas_, NULL);

    double res = 0;
    for(int i= 0; i < 15; i++){
        res += mas[i];
    }

    env->ReleaseDoubleArrayElements(mas_, mas, 0);

    return env->NewStringUTF("");
}

extern "C"
jstring
Java_com_example_vladbirukov_player_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
