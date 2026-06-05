#!/bin/bash
# CineramaMaps iOS Setup Script - Run this on Mac
# Usage: open Terminal, cd to this folder, run: bash setup_ios.sh

echo "=== CineramaMaps iOS Setup ==="
echo ""

# Step 1: Install CocoaPods if not installed
if ! command -v pod &> /dev/null; then
    echo "Installing CocoaPods..."
    sudo gem install cocoapods
else
    echo "CocoaPods already installed ✅"
fi

# Step 2: Install dependencies
echo ""
echo "Installing dependencies (this takes 2-3 minutes)..."
pod install

# Step 3: Done
echo ""
echo "=== DONE ==="
echo ""
echo "Now do this:"
echo "1. Double-click 'Cinerama Maps.xcworkspace' (NOT .xcodeproj)"
echo "2. In Xcode top-left, click 'Cinerama Maps' > Signing & Capabilities"
echo "3. Check 'Automatically manage signing'"
echo "4. Team: select your Apple ID"
echo "5. If bundle ID error, change to: com.test.CineramaMap"
echo "6. Connect iPhone via USB, select it in top bar"
echo "7. Click Play ▶ button"
echo "8. If 'Untrusted Developer' on iPhone: Settings > General > VPN & Device Management > Trust"
echo ""
echo "Test login: securetechs1@gmail.com (ask Imtiaz for OTP)"
