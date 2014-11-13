echo "This script generates new keys" 
echo "This should probably not be used, it is more just documentation about how the keys were generated"
echo "Are you sure you want to run it? [y/n]"
read DO_CONTINUE
if [ "$DO_CONTINUE" != "y" ]
then
    exit
fi

source build.info

echo "Generating RELEASE key, enter a secure password!"
keytool -genkey -v -keystore ${RELEASE_KEYSTORE} -alias ${RELEASE_ALIAS} -keyalg RSA -keysize 2048 -validity 10000

echo "Now that a new release key is generated, you must update the Google APIs Key"
echo "Follow the instructions here:"
echo "https://developers.google.com/maps/documentation/android/start"

echo "Exiting..."
exit 0

# To check the SHA1 run this:
keytool -list -v -keystore $RELEASE_KEYSTORE -alias $RELEASE_ALIAS
