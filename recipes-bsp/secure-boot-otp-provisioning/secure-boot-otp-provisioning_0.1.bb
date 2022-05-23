# Copyright (c) Siemens AG, 2022
#
# Authors:
#  Su Bao Cheng <baocheng.su@siemens.com>
#
# SPDX-License-Identifier: MIT
#

inherit dpkg

DESCRIPTION = "Secure Boot OTP key provisioning tool"

DEBIAN_BUILD_DEPENDS = "openssl, u-boot-tools, device-tree-compiler"

SRC_URI = " \
    file://its \
    file://keys \
    file://make-otpcmd.sh \
    file://rules"

do_prepare_build[cleandirs] += "${S}/debian"
do_prepare_build() {
    deb_debianize
    cd ${S}
    cp -rPf ../its ${S}/its
    ln -sf ../keys ${S}/keys
    ln -sf ../make-otpcmd.sh ${S}
    echo "otpcmd.bin /usr/lib/secure-boot-otp-provisioning/" > \
            ${S}/debian/secure-boot-otp-provisioning.install
}

OTPCMD_MODE ?= "provision"
OTP_MPK ?= "./keys/custMpk.pem"
OTP_SMPK ?= "./keys/custSmpk.pem"
OTP_BMPK ?= ""
OTPCMD_KEYS ?= "${OTP_MPK} ${OTP_SMPK} ${OTP_BMPK}"

dpkg_runbuild_prepend() {
    export OPTCMD_PARAS="${OTPCMD_MODE} ${OTPCMD_KEYS}"
}
