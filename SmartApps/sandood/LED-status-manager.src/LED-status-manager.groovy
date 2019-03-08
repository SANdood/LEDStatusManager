/**
 *  LED Status Manager Monitor
 *
 *  Copyright 2018 BARRY BURKE
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 * 	Version History
 * 	1.0.0	03/07/2019	Initial release
 *	1.0.1	03/08/2019	Added lamps off in Sleeep Mode
 */
def getVersionNum() { return "1.0.0" }
private def getVersionLabel() { return "${app.name} (${app.label}), v${getVersionNum()}" }
 
definition(
    name: 			"LED Status Manager",
    namespace: 		"sandood",
    author: 		"barry@thestorageanarchist.com",
    description: 	"Monitor various events and update Homeseer HS-WD200+ Status LEDs",
    category: 		"Safety & Security",
    iconUrl: 		"https://raw.githubusercontent.com/SANdood/LEDStatusManager/master/images/LEDStatus@1x.png",
    iconX2Url: 		"https://raw.githubusercontent.com/SANdood/LEDStatusManager/master/images/LEDStatus@2x.png",
    iconX3Url: 		"https://raw.githubusercontent.com/SANdood/LEDStatusManager/master/images/LEDStatus@3x.png")
      
preferences {
    page name:		"mainPage"
}

def HSColorMap() {
	return [0:'Off', 1:'Red', 2:'Green', 3:'Blue', 4:'Magenta', 5:'Yellow', 6:'Cyan', 7:'White']
}

def mainPage() {
	dynamicPage(name: "mainPage", title: getVersionLabel(), install: true, uninstall: true) {
    	section(title: "Name for this ${app.name}") {
        	label title: "Name", required: true, defaultValue: "LED Status Manager"
        }
    	if (!settings?.tempDisable) {
			section("Homeseer HS-WD200+ Dimmers...") {
				input "HSSwitches", "capability.switchLevel", title: "Homeseer dimmers for Status Display", require: true, multiple: true
                input "HSStatusColor", "enum", title: "Homeseer Dimmer Status Color", required: true, options: HSColorMap()
			}
            section("SHM Status...") {
                input "SHMLED", "number", title: "SHM LED #", range: "1..7", multiple: false, required: false, submitOnChange: true
                if (settings.SHMLED != null) {
					input "SHMStayColor", "enum", title: "SHM Armed Stay Color", required: false, options: HSColorMap()
					input "SHMAwayColor", "enum", title: "SHM Armed Away Color", required: false, options: HSColorMap()
					input "SHMOffColor", "enum", title: "SHM Off Color", required: false, options: HSColorMap()
				}
			}
            section("Location Mode...") {
                input "locationModeLED", "number", title: "Location Mode LED #", range: "1..7", multiple: false, required: false, submitOnChange: true
                if (settings.locationModeLED != null) {
					input "locModeHomeColor", "enum", title: "Mode Home Color", required: false, options: HSColorMap()
					input "locModeAwayColor", "enum", title: "Mode Away Color", required: false, options: HSColorMap()
					input "locModeSleepColor", "enum", title: "Mode Sleep Color", required: false, options: HSColorMap(), submitOnChange: true
                    if (settings.locModeSleepColor != null) {
                    	input "statusOffModeSleep", "bool", title: "Status LEDs off in Sleep Mode", required: true, submitOnChange: true
                        if (settings.statusOffModeSleep) {
                        	input "statusOffDelay", "number", title: "LEDs off delay (minutes)", required: true, defaultValue: 10
                        }
                    }
					input "locModeVacationColor", "enum", title: "Mode Vacation Color", required: false, options: HSColorMap()
				}
            }
            section("Door 1...") {
				input "doorOneLED", "number", title: "Door 1 LED #", range: "1..7", multiple: false, required: false, submitOnChange: true
            	if (settings.doorOneLED != null) {
					input "doorOneSensor", "capability.contactSensor", title: "Door 1 Contact Sensor", multiple: false, required: true
					input "doorOneOpenColor", "enum", title: "Door 1 Open Color", required: true, options: HSColorMap()
					input "doorOneClosedColor", "enum", title: "Door 1 Closed Color", required: true, options: HSColorMap()
					input "doorOneLock", "capability.lock", title: "Door 1 Lock", multiple: false, required: false, submitOnChange: true
					if (settings.doorOneLock != null) {
						input "doorOneLockedColor", "enum", title: "Door 1 Locked Color", required: true, options: HSColorMap()
					}
				}
            }
			section("Door 2...") {
				input "doorTwoLED", "number", title: "Door 2 LED #", range: "1..7", multiple: false, required: false, submitOnChange: true
            	if (settings.doorTwoLED != null) {
					input "doorTwoSensor", "capability.contactSensor", title: "Door 2 Contact Sensor", multiple: false, required: true
					input "doorTwoOpenColor", "enum", title: "Door 2 Open Color", required: true, options: HSColorMap()
					input "doorTwoClosedColor", "enum", title: "Door 2 Closed Color", required: true, options: HSColorMap()
					input "doorTwoLock", "capability.lock", title: "Door 2 Lock", multiple: false, required: false, submitOnChange: true
					if (settings.doorTwoLock != null) {
						input "doorTwoLockedColor", "enum", title: "Door 2 Locked Color", required: true, options: HSColorMap()
					}
				}
            }
			section("Door 3...") {
				input "doorThreeLED", "number", title: "Door 3 LED #", range: "1..7", multiple: false, required: false, submitOnChange: true
            	if (settings.doorThreeLED != null) {
					input "doorThreeSensor", "capability.contactSensor", title: "Door 3 Contact Sensor", multiple: false, required: true
					input "doorThreeOpenColor", "enum", title: "Door 3 Open Color", required: true, options: HSColorMap()
					input "doorThreeClosedColor", "enum", title: "Door 3 Closed Color", required: true, options: HSColorMap()
					input "doorThreeLock", "capability.lock", title: "Door 3 Lock", multiple: false, required: false, submitOnChange: true
					if (settings.doorThreeLock != null) {
						input "doorThreeLockedColor", "enum", title: "Door 3 Locked Color", required: true, options: HSColorMap()
					}
				}
            }
			section("Door 4...") {
				input "doorFourLED", "number", title: "Door 4 LED #", range: "1..7", multiple: false, required: false, submitOnChange: true
            	if (settings.doorFourLED != null) {
					input "doorFourSensor", "capability.contactSensor", title: "Door 4 Contact Sensor", multiple: false, required: true
					input "doorFourOpenColor", "enum", title: "Door 4 Open Color", required: true, options: HSColorMap()
					input "doorFourClosedColor", "enum", title: "Door 4 Closed Color", required: true, options: HSColorMap()
					input "doorFourLock", "capability.lock", title: "Door 4 Lock", multiple: false, required: false, submitOnChange: true
					if (settings.doorFourLock != null) {
						input "doorFourLockedColor", "enum", title: "Door 4 Locked Color", required: true, options: HSColorMap()
					}
				}
            }
			section("Door 5...") {
				input "doorFiveLED", "number", title: "Door 5 LED #", range: "1..7", multiple: false, required: false, submitOnChange: true
            	if (settings.doorFiveLED != null) {
					input "doorFiveSensor", "capability.contactSensor", title: "Door 5 Contact Sensor", multiple: false, required: true
					input "doorFiveOpenColor", "enum", title: "Door 5 Open Color", required: true, options: HSColorMap()
					input "doorFiveClosedColor", "enum", title: "Door 5 Closed Color", required: true, options: HSColorMap()
					input "doorFiveLock", "capability.lock", title: "Door 5 Lock", multiple: false, required: false, submitOnChange: true
					if (settings.doorFiveLock != null) {
						input "doorFiveLockedColor", "enum", title: "Door 5 Locked Color", required: true, options: HSColorMap()
					}
				}
            }
			
        } else {
        	section("Disabled") {
        		// We are currently disabled
            	paragraph "WARNING: Temporarily Disabled as requested. Turn back on below to re-activate."
            }
        }
        section(title: "Temporarily Disable?") {
           	input "tempDisable", "bool", title: "Temporarily disable this Monitor? ", defaultValue: false, required: false, description: "", submitOnChange: true            
        }
        section (getVersionLabel())
    }
}

def installed()
{
    log.debug "${app.label} installed with settings: ${settings}"
    if (!settings?.tempDisable) initialize( 'installed' )
}

def updated()
{
    log.debug "${app.label}' updated with settings: ${settings}"
    unsubscribe()
    unschedule()
    if (!settings?.tempDisable) {
    	initialize( 'updated' )
    }
}

def initialize( why )
{
	if (settings.SHMLED) subscribe(location, 'alarmSystemStatus', SHMChangeHandler)
	if (settings.locationModeLED) subscribe(location, 'mode', modeChangeHandler)
	if (settings.doorOneLED) {
		subscribe(settings.doorOneSensor, 'contact', contactChangeHandler)
		if (settings.doorOneLock) subscribe(settings.doorOneLock, 'lock', lockChangeHandler)
	}
	if (settings.doorTwoLED) {
		subscribe(settings.doorTwoSensor, 'contact', contactChangeHandler)
		if (settings.doorTwoLock) subscribe(settings.doorTwoLock, 'lock', lockChangeHandler)
	}
	if (settings.doorThreeLED) {
		subscribe(settings.doorThreeSensor, 'contact', contactChangeHandler)
		if (settings.doorThreeLock) subscribe(settings.doorThreeLock, 'lock', lockChangeHandler)
	}
	if (settings.doorFourLED) {
		subscribe(settings.doorFourSensor, 'contact', contactChangeHandler)
		if (settings.doorFourLock) subscribe(settings.doorFourLock, 'lock', lockChangeHandler)
	}
	if (settings.doorFiveLED) {
		subscribe(settings.doorFiveSensor, 'contact', contactChangeHandler)
		if (settings.doorFiveLock) subscribe(settings.doorFiveLock, 'lock', lockChangeHandler)
	}
    subscribe(app, appTouch)
    refresh()
}

def refresh() {
	SHMChangeHandler(null)
    modeChangeHandler(null)
    contactChangeHandler(null)
}

def appTouch( evt ) {
	log.trace "appTouch()"
    refresh()
    // runIn(10, setSwitchNormal, [overwrite: true])
}

def updateLed( led, color, blink = false ) {
	log.info "Setting LED #${led} to ${HSColorMap()[color.toInteger()]}, blink = ${blink}"
	HSSwitches.each { sw ->
		sw.setStatusLed( led.toInteger(), color.toInteger(), (blink ? 1 : 0))
	}
}

def setSwitchNormal() {
	HSSwitches.each { sw ->
    	sw.setSwitchModeNormal()
    }
}

def setSwitchStatus() {
	HSSwitches.each { sw ->
    	sw.setSwitchModeStatus()
    }
}

def SHMChangeHandler(evt = null) {
	def theValue
    def theColor
    if (evt) {
    	log.trace "SHMChangeHandler(${evt.name}, ${evt.value})"
        theValue = evt.value
    } else {
        theValue = location.currentValue('alarmSystemStatus')
        log.trace "SHMChangeHandler(${theValue})"
    }
	switch (theValue) {
		case 'Away': 
			if (settings.SHMAwayColor) 	theColor = settings.SHMAwayColor
			break;
		case 'Stay':
			if (settings.SHMStayColor) 	theColor = settings.SHMStayColor
			break;
		case 'off':
			if (settings.SHMOffColor)	theColor = settings.SHMOffColor
			break;
		default: 
			theColor = 0		// off
			break;
	}
    updateLed(settings.SHMLED, theColor, false)
    if (settings.statusOffModeSleep && (location.mode == 'Sleep')) runIn( 10, setSwitchNormal, [overwrite: true] ) 
}

def modeChangeHandler(evt = null) {
	def theValue
    def theColor
    if (evt) {
    	log.trace "modeChangeHandler(${evt.name}, ${evt.value}"
        theValue = evt.value
    } else {
    	theValue = location.mode
        log.trace "modeChangeHandler(${theValue})"
    }
	switch (theValue) {
		case 'Home':
			if (settings.locModeHomeColor) 		theColor = settings.locModeHomeColor
			break;
		case 'Away':
			if (settings.locModeAwayColor) 		theColor = settings.locModeAwayColor
			break;
		case 'Sleep':
			if (settings.locModeSleepColor) 	theColor = settings.locModSleepColor
			break;
		case 'Vacation':
			if (settings.locModeVacationColor)	theColor = settings.locModeVacationColor
			break;
		default:
			theColor = 0 // off
			break;
	}
    updateLed(settings.locationModeLED, theColor, false)
    
    if (settings.statusOffModeSleep) {
    	if (theValue == 'Sleep') {
        	if (settings.statusOffDelay != 0) {
            	runIn((settings.statusOffDelay.toInteger() * 60), setSwitchNormal, [overwrite: true])
            } else {
            	setSwitchNormal()
            }
        } else {
        	// Not in Sleep mode any more - turn on the status LEDs again
        	setSwitchStatus()
        }
    }
}

def contactChangeHandler(evt) {
	def theDoor
    def theValue
    int doorIndex
    def theColor = 0		// Off
    def theLock
    def isLocked = false
	if (evt) {
        log.trace "contactChangeHandler(${evt.device.displayName}, ${evt.name}, ${evt.value})"
        if 		(evt.device.deviceNetworkId == settings?.doorOneSensor?.deviceNetworkId) 	{ theDoor = 'doorOne'; 		doorIndex = 5; }
        else if (evt.device.deviceNetworkId == settings?.doorTwoSensor?.deviceNetworkId) 	{ theDoor = 'doorTwo'; 		doorIndex = 6; }
        else if (evt.device.deviceNetworkId == settings?.doorThreeSensor?.deviceNetworkId)	{ theDoor = 'doorThree'; 	doorIndex = 3; }
        else if (evt.device.deviceNetworkId == settings?.doorFourSensor?.deviceNetworkId)	{ theDoor = 'doorFour'; 	doorIndex = 2; }
        else if (evt.device.deviceNetworkId == settings?.doorFiveSensor?.deviceNetworkId) 	{ theDoor = 'doorFive'; 	doorIndex = 1; }
        if (!theDoor) {
            log.warn "Unknown contact sensor changed"
            return
        }
        theLock = settings."${theDoor}Lock"
        if (theLock != null) {
            isLocked = (theLock.currentValue('lock') == 'locked')
        }
        switch(evt.value){
            case 'open':
                if (settings."${theDoor}OpenColor") updateLed(doorIndex, settings."${theDoor}OpenColor", false)
                break;
            case 'closed':
                if (settings."${theDoor}ClosedColor") theColor = settings."${theDoor}ClosedColor"
                if ((theLock != null) && isLocked && settings."${theDoor}LockedColor") theColor = settings."${theDoor}LockedColor"
                updateLed(doorIndex, theColor, false)
                break;
        }
    } else {
    	log.trace "contactChangeHandler(null)"
    	// Need to refresh ALL the door indicators
        theDoor = 'doorOne'
        isLocked = false
        if (settings."${theDoor}Sensor") {
        	doorIndex = 5
        	theValue = settings."${theDoor}Sensor".currentValue('contact')
            if (settings."${theDoor}OpenColor" && (theValue == 'open')) {
            	updateLed(doorIndex, settings."${theDoor}OpenColor", false)
            } else if (theValue == 'closed') {
            	theLock = settings."${theDoor}Lock"
        		if (theLock != null) {
            		isLocked = (theLock.currentValue('lock') == 'locked')
        		}
                if (settings."${theDoor}ClosedColor") theColor = settings."${theDoor}ClosedColor"
                if ((theLock != null) && isLocked && settings."${theDoor}LockedColor") theColor = settings."${theDoor}LockedColor"
                updateLed(doorIndex, theColor, false)
            } else {
            	updateLed(doorIndex, 0, false)
            }
        }
        theDoor = 'doorTwo'
        isLocked = false
        if (settings."${theDoor}Sensor") {
        	doorIndex = 4
        	theValue = settings."${theDoor}Sensor".currentValue('contact')
            if (settings."${theDoor}OpenColor" && (theValue == 'open')) {
            	updateLed(doorIndex, settings."${theDoor}OpenColor", false)
            } else if (theValue == 'closed') {
            	theLock = settings."${theDoor}Lock"
        		if (theLock != null) {
            		isLocked = (theLock.currentValue('lock') == 'locked')
        		}
                if (settings."${theDoor}ClosedColor") theColor = settings."${theDoor}ClosedColor"
                if ((theLock != null) && isLocked && settings."${theDoor}LockedColor") theColor = settings."${theDoor}LockedColor"
                updateLed(doorIndex, theColor, false)
            } else {
            	updateLed(doorIndex, 0, false)
            }
        }
        theDoor = 'doorThree'
        isLocked = false
        if (settings."${theDoor}Sensor") {
        	doorIndex = 3
        	theValue = settings."${theDoor}Sensor".currentValue('contact')
            if (settings."${theDoor}OpenColor" && (theValue == 'open')) {
            	updateLed(doorIndex, settings."${theDoor}OpenColor", false)
            } else if (theValue == 'closed') {
            	theLock = settings."${theDoor}Lock"
        		if (theLock != null) {
            		isLocked = (theLock.currentValue('lock') == 'locked')
        		}
                if (settings."${theDoor}ClosedColor") theColor = settings."${theDoor}ClosedColor"
                if ((theLock != null) && isLocked && settings."${theDoor}LockedColor") theColor = settings."${theDoor}LockedColor"
                updateLed(doorIndex, theColor, false)
            } else {
            	updateLed(doorIndex, 0, false)
            }
        }
        theDoor = 'doorFour'
        isLocked = false
        if (settings."${theDoor}Sensor") {
        	doorIndex = 2
        	theValue = settings."${theDoor}Sensor".currentValue('contact')
            if (settings."${theDoor}OpenColor" && (theValue == 'open')) {
            	updateLed(doorIndex, settings."${theDoor}OpenColor", false)
            } else if (theValue == 'closed') {
            	theLock = settings."${theDoor}Lock"
        		if (theLock != null) {
            		isLocked = (theLock.currentValue('lock') == 'locked')
        		}
                if (settings."${theDoor}ClosedColor") theColor = settings."${theDoor}ClosedColor"
                if ((theLock != null) && isLocked && settings."${theDoor}LockedColor") theColor = settings."${theDoor}LockedColor"
                updateLed(doorIndex, theColor, false)
            } else {
            	updateLed(doorIndex, 0, false)
            }
        }
        theDoor = 'doorFive'
        isLocked = false
        if (settings."${theDoor}Sensor") {
        	doorIndex = 1
        	theValue = settings."${theDoor}Sensor".currentValue('contact')
            if (settings."${theDoor}OpenColor" && (theValue == 'open')) {
            	updateLed(doorIndex, settings."${theDoor}OpenColor", false)
            } else if (theValue == 'closed') {
            	theLock = settings."${theDoor}Lock"
        		if (theLock != null) {
            		isLocked = (theLock.currentValue('lock') == 'locked')
        		}
                if (settings."${theDoor}ClosedColor") theColor = settings."${theDoor}ClosedColor"
                if ((theLock != null) && isLocked && settings."${theDoor}LockedColor") theColor = settings."${theDoor}LockedColor"
                updateLed(doorIndex, theColor, false)
            } else {
            	updateLed(doorIndex, 0, false)
            }
        }
    }
    if (settings.statusOffModeSleep && (location.mode == 'Sleep')) runIn( 10, setSwitchNormal, [overwrite: true] ) 
}

def lockChangeHandler(evt) {
	log.trace "lockChangeHandler(${evt.device.displayName}, ${evt.name}, ${evt.value})"
	def theDoor = null
	int doorIndex
	if 		(evt.device.deviceNetworkId == settings?.doorOneLock?.deviceNetworkId) 		{ theDoor = 'doorOne'; 		doorIndex = 5; }
	else if (evt.device.deviceNetworkId == settings?.doorTwoLock?.deviceNetworkId) 		{ theDoor = 'doorTwo'; 		doorIndex = 6; }
	else if (evt.device.deviceNetworkId == settings?.doorThreeLock?.deviceNetworkId)	{ theDoor = 'doorThree'; 	doorIndex = 3; }
	else if (evt.device.deviceNetworkId == settings?.doorFourLock?.deviceNetworkId) 	{ theDoor = 'doorFour'; 	doorIndex = 2; }
	else if (evt.device.deviceNetworkId == settings?.doorFiveLock?.deviceNetworkId) 	{ theDoor = 'doorFive'; 	doorIndex = 1; }
	if (theDoor == null) {
		log.warn "Unknown lock changed ${theDoor} ${doorIndex}"
		return
	}
	def theColor = 0
	if (evt.value == 'locked') {
		if (settings."${theDoor}LockedColor") theColor = settings."${theDoor}LockedColor"
		if ((settings."${theDoor}Sensor".currentValue('contact') == 'open') && (settings."${theDoor}OpenColor")) {
			theColor = settings."${theDoor}OpenColor"
		}
        log.debug "locked ${theColor}"
	} else {
    	def theContact = settings?."${theDoor}Sensor"?.currentValue('contact')
		if (settings."${theDoor}ClosedColor" && (theContact == 'closed')) {
			theColor = settings."${theDoor}ClosedColor"
		} else if (settings."${theDoor}OpenColor" && (theContact == 'open')) {
			theColor = settings."${theDoor}OpenColor"
		}
        log.debug "unlocked ${theColor}"
	}
	updateLed(doorIndex, theColor, false)
    if (settings.statusOffModeSleep && (location.mode == 'Sleep')) runIn( 10, setSwitchNormal, [overwrite: true] ) 
}
