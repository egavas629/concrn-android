# get our key info
source build.info || { echo "Failed to find build.info"; exit 1; }

echo "=================="
echo "Starting the build"
echo "=================="
ant clean
ant release
assertIsZero $?

echo "========================"
echo "Signing the compiled APK"
echo "keystore: $RELEASE_KEYSTORE"
echo "key: $RELEASE_ALIAS"
echo "========================"
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore $RELEASE_KEYSTORE $UNSIGNED_RELEASE_APK $RELEASE_ALIAS
assertIsZero $?

echo "============================"
echo "Zipaligning for optimization"
echo "============================"
rm -rf $OUTPUT_APK
zipalign -v 4 $UNSIGNED_RELEASE_APK $OUTPUT_RELEASE_APK
assertIsZero $?

echo "================================="
echo "Your final APK is at: $OUTPUT_RELEASE_APK"
echo "================================="
