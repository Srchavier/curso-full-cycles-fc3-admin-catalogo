/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.admin.catalogo.application;

import java.util.List;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class UseCaseTest implements BeforeEachCallback{
    
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
       Mockito.reset(getMocks().toArray());
        
    }

    public abstract List<Object> getMocks();
}
