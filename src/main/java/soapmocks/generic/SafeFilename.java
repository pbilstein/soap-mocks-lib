/*
Copyright 2016 Peter Bilstein

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package soapmocks.generic;

public final class SafeFilename {

    private static final String ALLOWED_FILENAME_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ._-0123456789";
    
    public String make(String fileName)
    {
	fileName = fileName.replaceAll("ä", "ae");
	fileName = fileName.replaceAll("ö", "oe");
	fileName = fileName.replaceAll("ü", "ue");
	fileName = fileName.replaceAll("Ä", "AE");
	fileName = fileName.replaceAll("Ö", "OE");
	fileName = fileName.replaceAll("Ü", "UE");
        char[] newFileName = fileName.toCharArray();
        for (int i = 0; i < newFileName.length; i++)
        {
            if (!ALLOWED_FILENAME_CHARS.contains(newFileName[i]+"")) {
                newFileName[i] = '_';
            }
        }
        return new String(newFileName);
    }
}
