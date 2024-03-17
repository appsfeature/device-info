package com.deviceinfo.cpu;

import android.os.Build;
import android.os.Build.VERSION;

import com.deviceinfo.util.DIValidityCheck;
import com.deviceinfo.util.EasyDeviceInfo;
import com.deviceinfo.util.DILogger;
import com.deviceinfo.util.DITimeLogger;
import com.deviceinfo.util.DIUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CPU {

    public CPU() {
    }


    public HashMap<String, String> getInfo() {
        long startTime = DITimeLogger.getStartTime();
        HashMap<String, String> info = getCPUInfo();

        try {
            info.put("supported_32", getStringSupported32bitABIS());
            info.put("supported_64", getStringSupported64bitABIS());
            info.put("supported_ABIS", getStringSupportedABIS());
            info.put("supported_ABIS_details", Arrays.toString(getSupportedABIS()));
            info.put("num_of_cores", String.valueOf(getNumCores()));
        } catch (Exception e) {
            DILogger.e(e.toString());
            info.put("exception", e.toString());
        }
        DITimeLogger.timeLogging("CPU", startTime);
        return info;
    }

    public static String getCPUDetails() {

        String cpuDetails = "";
        try {
            ProcessBuilder processBuilder;
            String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
            InputStream is;
            Process process;
            byte[] bArray;
            bArray = new byte[1024];

            processBuilder = new ProcessBuilder(DATA);

            process = processBuilder.start();

            is = process.getInputStream();

            while (is.read(bArray) != -1) {
                cpuDetails = cpuDetails + new String(bArray);   //Stroing all the details in cpuDetails
            }
            is.close();

        } catch (IOException ex) {
            DILogger.e(DIUtility.ERROR_TAG, ex.getMessage());
            cpuDetails = ex.getMessage();
        }
        return cpuDetails;
    }


    public static HashMap<String, String> getCPUInfo() {
        HashMap<String, String> output = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/cpuinfo"));
            String str;

            while ((str = br.readLine()) != null) {

                String[] data = str.split(":");

                if (data.length > 1) {

                    String key = data[0].trim().replace(" ", "_");
                    if (key.equals("model_name")) key = "cpu_model";

                    String value = data[1].trim();

                    if (key.equals("cpu_model"))
                        value = value.replaceAll("\\s+", " ");

                    output.put(key, value);

                }
            }

            br.close();
        } catch (FileNotFoundException e) {
            DILogger.e(e.toString());
        } catch (IOException e) {
            DILogger.e(e.toString());
        } catch (Exception e) {
            DILogger.e(e.toString());
        }
        return output;

    }

    public static int getNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                return Pattern.matches("cpu[0-9]+", pathname.getName());
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            //Default to return 1 core
            return 1;
        }
    }

    /**
     * Gets string supported 32 bit abis.
     *
     * @return the string supported 32 bit abis
     */
    public final String getStringSupported32bitABIS() {
        String result = null;
        try {
            if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final String[] supportedABIS = Build.SUPPORTED_32_BIT_ABIS;

                final StringBuilder supportedABIString = new StringBuilder();
                if (supportedABIS.length > 0) {
                    for (final String abis : supportedABIS) {
                        supportedABIString.append(abis).append('_');
                    }
                    supportedABIString.deleteCharAt(supportedABIString.lastIndexOf("_"));
                } else {
                    supportedABIString.append("");
                }

                result = supportedABIString.toString();
            }
            return DIValidityCheck.checkValidData(
                    DIValidityCheck.handleIllegalCharacterInResult(result));
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Gets string supported 64 bit abis.
     *
     * @return the string supported 64 bit abis
     */
    public final String getStringSupported64bitABIS() {
        try {
            String result = null;
            if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final String[] supportedABIS = Build.SUPPORTED_64_BIT_ABIS;

                final StringBuilder supportedABIString = new StringBuilder();
                if (supportedABIS.length > 0) {
                    for (final String abis : supportedABIS) {
                        supportedABIString.append(abis).append('_');
                    }
                    supportedABIString.deleteCharAt(supportedABIString.lastIndexOf("_"));
                } else {
                    supportedABIString.append("");
                }
                result = supportedABIString.toString();
            }
            return DIValidityCheck.checkValidData(
                    DIValidityCheck.handleIllegalCharacterInResult(result));
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Gets string supported abis.
     *
     * @return the string supported abis
     */
    public final String getStringSupportedABIS() {
        try {
            String result = null;
            if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                final String[] supportedABIS = Build.SUPPORTED_ABIS;
                final StringBuilder supportedABIString = new StringBuilder();
                if (supportedABIS.length > 0) {
                    for (final String abis : supportedABIS) {
                        supportedABIString.append(abis).append('_');
                    }
                    supportedABIString.deleteCharAt(supportedABIString.lastIndexOf("_"));
                } else {
                    supportedABIString.append("");
                }
                result = supportedABIString.toString();
            }
            return DIValidityCheck.checkValidData(
                    DIValidityCheck.handleIllegalCharacterInResult(result));
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Get supported 32 bit abis string [ ].
     *
     * @return the string [ ]
     */
    public final String[] getSupported32bitABIS() {
        String[] result = {EasyDeviceInfo.NOT_FOUND_VAL};
        if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            result = Build.SUPPORTED_32_BIT_ABIS;
        }
        return DIValidityCheck.checkValidData(result);
    }

    /**
     * Get supported 64 bit abis string [ ].
     *
     * @return the string [ ]
     */
    public final String[] getSupported64bitABIS() {
        String[] result = {EasyDeviceInfo.NOT_FOUND_VAL};
        if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            result = Build.SUPPORTED_64_BIT_ABIS;
        }
        return DIValidityCheck.checkValidData(result);
    }

    /**
     * Get supported abis string [ ].
     *
     * @return the string [ ]
     */
    public final String[] getSupportedABIS() {
        try {
            String[] result = {EasyDeviceInfo.NOT_FOUND_VAL};
            if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                result = Build.SUPPORTED_ABIS;
            }
            return DIValidityCheck.checkValidData(result);
        } catch (Exception e) {
            DILogger.e(DIUtility.ERROR_TAG, e.getMessage());
            return new String[]{e.getMessage()};
        }
    }
}
