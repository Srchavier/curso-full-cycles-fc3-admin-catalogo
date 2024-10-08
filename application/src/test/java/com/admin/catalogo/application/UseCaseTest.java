/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.admin.catalogo.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.admin.catalogo.domain.Identifier;

@ExtendWith(MockitoExtension.class)
public abstract class UseCaseTest implements BeforeEachCallback{
    
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
       Mockito.reset(getMocks().toArray());
        
    }

    public abstract List<Object> getMocks();


    protected Set<String> asString(final Set<? extends Identifier> anIds) {
        return anIds.stream().map(Identifier::getValue).collect(Collectors.toSet());
    }

    protected List<String> asString(final List<? extends Identifier> anIds) {
        return anIds.stream().map(Identifier::getValue).toList();
    }
}
