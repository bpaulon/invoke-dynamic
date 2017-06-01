project {

    modelVersion '4.0.0'    
    groupId 'bcp.invokedynamic'
    artifactId 'invokedynamic-test'
    version '0.0.1-SNAPSHOT'
  
    name 'Invoke dynamic of virtual method - Tests'
    
    properties {
        'project.build.sourceEncoding' 'UTF-8'
        'maven.compiler.source' '1.8'
        'maven.compiler.target' '1.8'
    }
    
    dependencies {
        dependency {
            groupId 'org.projectlombok'
            artifactId 'lombok'
            version '1.16.16'
            scope 'compile'
        }
        
        dependency {
            groupId 'org.slf4j'
            artifactId 'slf4j-simple'
            version '1.7.25'
        }
    }
      
  build {

    $execute(id: 'showInfo', phase: 'validate') { ec ->
      println "\n\nProject properties"
      println ec.getProject().getModel().getProperties()
      println 'Version : ' + ec.getProject().getModel().getVersion()
      println 'Group ID : ' + ec.getProject().getModel().getGroupId()
      println 'Artifact ID : ' + ec.getProject().getModel().getArtifactId()
      println 'Basedir : ' + ec.basedir()
      println "\n\n"
    }
    
    /*
     * It seems that the maven-compiler-plugin doesn't know how to pass a non-standard option
     * such as '-XDignore.symbol.file'to the Java compiler API. 
     * Force it to fork the javac compiler 
     */
    plugins {
      plugin {
        artifactId 'maven-compiler-plugin'
        version '3.5.1'
        configuration {
          fork 'true'
          compilerArgs {
            arg '-verbose'
            arg '-XDignore.symbol.file'
          }
        }
      }
    }           
  }
}