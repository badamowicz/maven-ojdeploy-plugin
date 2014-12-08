import java.util.*;

/**
 * Script checks executing ojdeploy with a given profile.
 */

File ear = null;
File war = null;
boolean success = true;

println "\n##################################################################################"
println ""
println "Starting Integration Test for ojdeploy using a profile.\n"
println ""
println "##################################################################################"

try {

    ear = new File("target/it/Ojdeploy-Profile-IT/ShuttleDemo/ViewController/deploy/ShuttleDemo_ViewController_webapp1.war");
    println "Checking file " + ear.getAbsolutePath() + " for existence."
    war = new File("target/it/Ojdeploy-Profile-IT/ShuttleDemo/deploy/ShuttleDemo_application1.ear");
    println "Checking file " + war.getAbsolutePath() + " for existence."

    if(!ear.exists() || !war.exists()) {
     
            success = false;
            println "At least on of the expected files was not generated!"
    }


    println "\n##################################################################################"
    println ""
    println "Integration test successful!\n"
    println ""
    println "##################################################################################"

    return success;
} catch(Exception e) {

    println "\n##################################################################################"
    println ""
    println "Integration test failed:\n" + e.toString()
    println ""
    println "##################################################################################"

    return false;
}