project {

    modelVersion '4.0.0'    
    groupId 'bcp.invokedynamic'
    artifactId 'invokedynamic-test'
    version '0.0.1-SNAPSHOT'
  
    name 'Invoke dynamic on virtual method tests'
    properties {
        'maven.compiler.source' 1.8
        'maven.compiler.target' 1.8
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
    //
    // Arbitrary Groovy code can be executed in any phase in the form of a dynamic plugin
    //
    $execute(id: 'hello', phase: 'validate') {
      println ""
      println "Hello! Rambling here ...."
      println ""
    }           
  }
}