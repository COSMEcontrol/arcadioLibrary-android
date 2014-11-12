package com.arcadio;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.arcadio.api.v1.service.PluginClientArcadio;
import com.arcadio.modelo.Cesta;
import com.arcadio.modelo.ItemVariable;

import java.util.ArrayList;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
}