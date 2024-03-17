# DeviceInfo

#### Library size is : 478Kb

Android library to get device information in a super easy way.
DeviceInfo library gives you details of Hardware & Software configurations of your Android device.
This detail specifications includes information of CPU, RAM, Storage, OS, Sensors, Core, Battery, Data Network, WiFi, SIM, Bluetooth, Display, Supported features, Manufacturer, Installed Apps.


  
## Setup
Add the token to $HOME/.gradle/gradle.properties
``` gradle
    authToken=jp_65cg6e0t51u6mt3uoif46rva4
```

Add this to your project build.gradle
``` gradle
allprojects {
    repositories {
        maven {
            url "https://jitpack.io"
            credentials { username authToken }
        }
    }
    ext {
        firebase_crashlytics_version = '2.9.9' 
    }
}
```
 
#### Dependency
[![](https://jitpack.io/v/org.bitbucket.amitresearchdev/DeviceInfo.svg)](https://jitpack.io/#org.bitbucket.amitresearchdev/DeviceInfo)
```gradle
dependencies {
    implementation 'org.bitbucket.amitresearchdev:DeviceInfo:x.y'
}
```

#### Usage method
In your activity class:
```java 
      DeviceInfo.newInstance(this)
          .disableBluetoothDetails()
          .disableApplicationDetails()
          .addCallback(new Callback<JSONObject>() {
              @Override
              public void onSuccess(JSONObject response) {
                  Logger.d("onSuccess",response.toString());
              }

              @Override
              public void onError(Exception e) {
                  Logger.d("onError",e.getMessage());
              }
          }).fetch();
```


#### Permissions Required
Add following Permission in your Manifest file if need the following details(Network, SIM, Bluetooth).
##Network
```xml
    Normal Permission
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
        <uses-permission android:name="android.permission.INTERNET" />

    Runtime Permission
        <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```
##SIM
```xml
    Runtime Permission
        <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
```
##Bluetooth
```xml
    Normal Permission
        <uses-permission android:name="android.permission.BLUETOOTH"/>
```


Device Info provides following information of your Android device which grouped as below.

##Android Device Information
```  
android os version name
display version
build board
brand name
host
build tags
build user
version code name
version incremental
version release
build device
device unique fingerprint
hardware
language
manufacturer
device model
product
radio version
screen display id
build version sdk
phone type
build id
build time
```
##Battery
```
technology
temperature
voltage
charging state
charging source
charged percentage
health
```
##Sensor
```
(Accelerometer, Gyroscope, Pedometer, Magnetometer, Step detector, Gravity, Motion, Rotation, Tilt, Gesture, Wakeup)
Available information regarding each sensor:
Sensor Name
Vendor
Version
```
##CPU
```
supported 32
supported 64
supported ABIS
supported ABIS details
num of cores
details
```
##Network
```
wifi enabled
bssid
connection status
ip v4 address
ip v6 address
data type
ssid
link speed
cell tower
```
##SIM
```
carrier
country
sim network locked
IMEI
IMSI
number of active sim
sim serial
sim subscription
```
##Storage
```
total ram
external memory available
total internal memory size
available internal memory size
total external memory size
available external memory size
```
##Bluetooth
```
has bluetooth le
has bluetooth le advertising
```
##Display
```
density
physical size
orientation
resolution
screen round
refresh rate
```
##Features
```
nfc
connected devices list
multi touch
```