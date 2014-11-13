# Concrn - Android
This readme will help you build and sign the Concrn app.

## Build Environment Setup

### Libraries

This requires:
 - Android SDK
 - Google Play Services
 - Android Support Library
 - ant

Follow the instructions from google on how to set those up.

After, you must set the following environment variables:
```
ANDROID_HOME=<your-android-sdk-root>
PATH=$PATH:$ANDROID_HOME/platform-tools
PATH=$PATH:$ANDROID_HOME/build-tools/<your-build-tools-version>/
CONCRN_HOME=<path-to-concrn>
```

Now that you have everything downloaded and set up, we need to link in the proper libraries.  Make sure you have the same version of SDK, Services, and Support Library.  Then, create a symbolic link to them parallel to this repository.
```
ln -s $ANDROID_HOME/extras/google/google_play_services/libproject/google-play-services_lib $CONCRN_HOME/../google-play-services_lib
ln -s $ANDROID_HOME/extras/android/support/v7/appcompat/ $CONCRN_HOME/../appcompat
```
Then run 'android update project' with the proper arguments.  For example, if all the libraries are R21, then run this:
```
cd $CONCRN_HOME
android update project -p . -t android-21
```
This will generate a bunch of local files, includeing the build.xml

### Keys

There are 2 keys that we deal with, the debug keys and the release keys.  

#### Debug key
The debug key is in the tree.  To use the debug key, we must put it where the android build system is set up to look.  So just run:
```
mv $CONCRN_HOME/debug.keystore ~/.android/debug.keystore
```

#### Release key
The release key is private, only the managers of this app have access to it.  Get it from one of them, and move it to the location:
```
$CONCRN_HOME/concrn-release.keystore
```

## Building

### Debug build

The only subtly here is that the Google API key is set to the release key so you must **comment out the release key, and uncomment the debug key in AndroidManifest.xml**.  Once you have done that, and setup the debug key from above, just run:

```
ant debug
```
The apk will be at: $CONCRN_HOME/bin/MainActivity-debug.apk

### Release build

Get the release key and password. Make sure that the Google API release key is active in AndroidManifest.xml, and run:
```
./build_release.sh
```
And enter the password when prompted.  The final apk will be at $CONCRN_HOME/bin/Concrn.apk

