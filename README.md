# DeviceInfo

#### Library size is : 478Kb

Android library to get device information in a super easy way.
DeviceInfo library gives you details of Hardware & Software configurations of your Android device.
This detail specifications includes information of CPU, RAM, Storage, OS, Sensors, Core, Battery, Data Network, WiFi, SIM, Bluetooth, Display, Supported features, Manufacturer, Installed Apps.


Add this to your project build.gradle
``` gradle
allprojects {
    repositories {
        maven {
            url "https://jitpack.io"
        }
    }
}
```
 
#### Dependency
[![](https://jitpack.io/v/appsfeature/device-info.svg)](https://jitpack.io/#appsfeature/device-info)
```gradle
dependencies {
    implementation 'com.github.appsfeature:device-info:x.y'
}
```

#### Usage method
In your activity class:
```java 
      DeviceInfo.getInstance()
              .setEnablePermissionRequiredInfo(true)
              .setDebugMode(true)
              .setPermission(Manifest.permission.ACCESS_FINE_LOCATION)
              .addCallback(new DeviceInfoCallback<DeviceInfoResult>() {
                  @Override
                  public void onSuccess(DeviceInfoResult response) {
                      printResponse(response);
                      Log.d("onSuccess",response.toString());
                  }

                  @Override
                  public void onError(Exception e) {
                      tvStatus.setText(e.getMessage());
                      Log.d("onError",e.getMessage());
                  }
              });

      // call this method from background main thread.
      DeviceInfo.getInstance().fetch(this);

      // call this method from background worker thread.
      DIResult result = DeviceInfo.getInstance().fetchEnqueue(this);
```


#### Permissions Required
Add following Permission in your Manifest file if need the following details(Network, SIM, Bluetooth).
##Network
```xml
    Normal Permission
        <uses-permission android:name="android.permission.INTERNET" />
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

    Runtime Permission
        <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
        <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
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
##Application
```xml
    Normal Permission
        <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES"
                tools:ignore="QueryAllPackagesPermission" />

        <queries>
            <intent>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.BROWSABLE" />
                <data android:scheme="https" />
            </intent>
            <intent>
                <action android:name="android.intent.action.VIEW" />
                <!-- If you don't know the MIME type in advance, set "mimeType" to "*/*". -->
                <data android:mimeType="application/pdf" />
            </intent>
            <intent>
                <action android:name="android.intent.action.TTS_SERVICE" />
            </intent>
            <intent>
                <action android:name="android.speech.RecognitionService" />
            </intent>
            <intent>
                <action android:name="android.media.browse.MediaBrowserService" />
            </intent>
            <intent>
                <action android:name="android.intent.action.SENDTO"/>
                <data android:scheme="smsto" android:host="*" />
            </intent>
        </queries>
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

```json
{
  "applicationInfo": [
    {
      "installedDate": 0,
      "lastModified": 0,
      "name": "Attempt to get length of null array"
    }
  ],
  "batteryInfo": {
    "charging_source": "Source-USB",
    "charging_source_index": "USB",
    "temperature": "31.7",
    "health": "Battery health Good",
    "technology": "Li-ion",
    "charging_state": "Charging",
    "charged_percentage": "80",
    "is_battery_present": "true",
    "voltage": "4052"
  },
  "bluetoothInfo": {
    "has_bluetooth_le": "YES",
    "has_bluetooth_le_advertising": "YES"
  },
  "cpuInfo": {
    "CPU_revision": "2",
    "Processor": "AArch64 Processor rev 2 (aarch64)",
    "num_of_cores": "8",
    "CPU_architecture": "8",
    "supported_ABIS": "arm64-v8a_armeabi-v7a_armeabi",
    "processor": "7",
    "supported_32": "armeabi-v7a_armeabi",
    "BogoMIPS": "38.40",
    "supported_64": "arm64-v8a",
    "CPU_part": "0x800",
    "Features": "fp asimd evtstrm aes pmull sha1 sha2 crc32",
    "CPU_implementer": "0x51",
    "Hardware": "Qualcomm Technologies, Inc SDM660",
    "CPU_variant": "0xa",
    "supported_ABIS_details": "[arm64-v8a, armeabi-v7a, armeabi]"
  },
  "deviceConfigInfo": {
    "current_date": "17-03-2024 | 10:01 pm",
    "running_on_emulator": "NO",
    "formatted_time": "10:01:46 pm",
    "formatted_up_time": "9:38:36 pm",
    "sd_card_available": "YES",
    "formatted_date": "17 Mar 2024",
    "device_ringer_mode": "NORMAL"
  },
  "deviceInfo": {
    "phone_no": "919876543210",
    "device_unique_fingerprint": "samsung/astarqltedx/astarqlte:10/QP1A.190711.020/G885FDXU5CWH2:user/release-keys",
    "android_base_os_version_name": "unknown",
    "device_model": "SM-G885F",
    "build_board": "sdm660",
    "version_release": "10",
    "build_time": "23-08-2023 | 11:12 am",
    "build_user": "dpi",
    "language": "en",
    "device_type": "PHONE",
    "phone_type_mod": "GSM",
    "manufacturer": "samsung",
    "build_device": "astarqlte",
    "host": "SWDJ5706",
    "radio_version": "G885FDXU5CVG1,G885FDXU5CVG1",
    "hardware": "qcom",
    "product": "astarqltedx",
    "phone_type": "GSM",
    "orientation": "PORTRAIT",
    "is_device_rooted": "false",
    "screen_display_id": "0",
    "version_incremental": "G885FDXU5CWH2",
    "brand_name": "samsung",
    "build_version_sdk": "29",
    "serial": "unknown",
    "build_id": "QP1A.190711.020",
    "display_version": "QP1A.190711.020.G885FDXU5CWH2",
    "android_os_version_name": "10",
    "version_code_name": "REL",
    "device_unique_id": "d179d6270b49fbb0",
    "system_boot_loader_version": "G885FDXU5CWH2",
    "build_tags": "release-keys"
  },
  "deviceMemoryInfo": {
    "total_internal_memory_size": "51.695747 Gb",
    "total_ram_memory_size": "5.565918 Gb",
    "available_internal_memory_size": "19.465942 Gb",
    "available_external_memory_size": "19.446411 Gb",
    "external_memory_available": "YES",
    "total_external_memory_size": "51.676216 Gb",
    "total_ram": "5.565918 Gb",
    "free_ram_memory_size": "2.6240234 Gb"
  },
  "deviceSimInfo": {
    "country": "in",
    "carrier": "jio_4g_|_jio",
    "IMSI": "unknown",
    "IMEI": "unknown",
    "number_of_active_sim": "1",
    "sim_subscription": "[{\"carrier_name\":\"JIO 4G | Jio\",\"country_iso\":\"in\",\"display_name\":\"Jio\",\"icc_id\":\"\",\"mcc\":405,\"number\":\"919876543210\",\"sim_slot_index\":0,\"subscription_id\":3}]",
    "sim_network_locked": "NO",
    "sim_serial": "unknown"
  },
  "displayInfo": {
    "orientation": "portrait",
    "density": "unknown",
    "screen_round": "false",
    "layout_direction": "0",
    "refresh_rate": "60.000004",
    "physical_size": "5.763741",
    "resolution": "2094x1080"
  },
  "featureInfo": {
    "multi_touch": "Supported",
    "connected_devices_list": "No device connected",
    "nfc": "Not available"
  },
  "networkInfo": {
    "wifi_enabled": "YES",
    "cell_tower": "{\"signal_strength\":\"-103\",\"gsm_cell_identity\":\"1372196\",\"mobile_country_code\":\"405\",\"mobile_network_code\":\"872\",\"location_area_code\":\"unknown\"}",
    "mac_address": "A6:E0:32:BE:A5:A2",
    "bssid": "20:0c:86:02:4a:d9",
    "connection_status": "Connected",
    "ip_v4_address": "192.168.1.4",
    "ip_v6_address": "2409:4110:1D:E02F:8000::",
    "data_type": "WiFi",
    "link_speed": "390 Mbps",
    "ssid": "\"AKR WiFi.5G\""
  },
  "sensorsInfo": [
    {
      "mFlags": 0
    },
    {
      "mFlags": 0
    },
    {
      "mFlags": 0
    },
    {
      "mFlags": 0
    },
    {
      "mFlags": 0
    },
    {
      "mFlags": 0
    },
    {
      "mFlags": 3
    },
    {
      "mFlags": 2
    },
    {
      "mFlags": 0
    },
    {
      "mFlags": 0
    },
    {
      "mFlags": 0
    },
    {
      "mFlags": 6
    },
    {
      "mFlags": 2
    },
    {
      "mFlags": 5
    },
    {
      "mFlags": 0
    },
    {
      "mFlags": 7
    },
    {
      "mFlags": 5
    },
    {
      "mFlags": 2
    },
    {
      "mFlags": 16
    },
    {
      "mFlags": 0
    },
    {
      "mFlags": 2
    },
    {
      "mFlags": 2
    },
    {
      "mFlags": 3
    },
    {
      "mFlags": 3
    },
    {
      "mFlags": 2
    },
    {
      "mFlags": 2
    },
    {
      "mFlags": 0
    }
  ]
}
```