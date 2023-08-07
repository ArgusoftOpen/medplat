#!/bin/bash
rm -f /apk_package/*.apk
cp /usr/android/sewa-android/build/outputs/apk/medplat/debug/*.apk /apk_package && exit
