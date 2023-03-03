// 
// Decompiled by Procyon v0.5.36
// 

package org.junit.rules;

import java.io.IOException;
import java.io.File;

public class TemporaryFolder extends ExternalResource
{
    private final File parentFolder;
    private File folder;
    
    public TemporaryFolder() {
        this(null);
    }
    
    public TemporaryFolder(final File parentFolder) {
        this.parentFolder = parentFolder;
    }
    
    @Override
    protected void before() throws Throwable {
        this.create();
    }
    
    @Override
    protected void after() {
        this.delete();
    }
    
    public void create() throws IOException {
        this.folder = this.createTemporaryFolderIn(this.parentFolder);
    }
    
    public File newFile(final String fileName) throws IOException {
        final File file = new File(this.getRoot(), fileName);
        if (!file.createNewFile()) {
            throw new IOException("a file with the name '" + fileName + "' already exists in the test folder");
        }
        return file;
    }
    
    public File newFile() throws IOException {
        return File.createTempFile("junit", null, this.getRoot());
    }
    
    public File newFolder(final String folder) throws IOException {
        return this.newFolder(new String[] { folder });
    }
    
    public File newFolder(final String... folderNames) throws IOException {
        File file = this.getRoot();
        for (int i = 0; i < folderNames.length; ++i) {
            final String folderName = folderNames[i];
            this.validateFolderName(folderName);
            file = new File(file, folderName);
            if (!file.mkdir() && this.isLastElementInArray(i, folderNames)) {
                throw new IOException("a folder with the name '" + folderName + "' already exists");
            }
        }
        return file;
    }
    
    private void validateFolderName(final String folderName) throws IOException {
        final File tempFile = new File(folderName);
        if (tempFile.getParent() != null) {
            final String errorMsg = "Folder name cannot consist of multiple path components separated by a file separator. Please use newFolder('MyParentFolder','MyFolder') to create hierarchies of folders";
            throw new IOException(errorMsg);
        }
    }
    
    private boolean isLastElementInArray(final int index, final String[] array) {
        return index == array.length - 1;
    }
    
    public File newFolder() throws IOException {
        return this.createTemporaryFolderIn(this.getRoot());
    }
    
    private File createTemporaryFolderIn(final File parentFolder) throws IOException {
        final File createdFolder = File.createTempFile("junit", "", parentFolder);
        createdFolder.delete();
        createdFolder.mkdir();
        return createdFolder;
    }
    
    public File getRoot() {
        if (this.folder == null) {
            throw new IllegalStateException("the temporary folder has not yet been created");
        }
        return this.folder;
    }
    
    public void delete() {
        if (this.folder != null) {
            this.recursiveDelete(this.folder);
        }
    }
    
    private void recursiveDelete(final File file) {
        final File[] files = file.listFiles();
        if (files != null) {
            for (final File each : files) {
                this.recursiveDelete(each);
            }
        }
        file.delete();
    }
}
