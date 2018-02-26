/*
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.drive.sample.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.metadata.CustomPropertyKey;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * An activity to illustrate how update or insert (if it does not exist) a custom property onto a
 * file.
 */
public class InsertUpdateCustomPropertyActivity extends BaseDemoActivity {
    private static final String TAG = "DeleteCustomProperty";

    @Override
    protected void onDriveClientReady() {
        pickTextFile()
                .addOnSuccessListener(this,
                        new OnSuccessListener<DriveId>() {
                            @Override
                            public void onSuccess(DriveId driveId) {
                                updateCustomProperty(driveId.asDriveFile());
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "No file selected", e);
                        showMessage(getString(R.string.file_not_selected));
                        finish();
                    }
                });
    }

    private void updateCustomProperty(DriveFile file) {
        // [START update_custom_property]
        CustomPropertyKey approvalPropertyKey =
                new CustomPropertyKey("approved", CustomPropertyKey.PUBLIC);
        CustomPropertyKey submitPropertyKey =
                new CustomPropertyKey("submitted", CustomPropertyKey.PUBLIC);
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                              .setCustomProperty(approvalPropertyKey, "yes")
                                              .setCustomProperty(submitPropertyKey, "no")
                                              .build();
        Task<Metadata> updateMetadataTask =
                getDriveResourceClient().updateMetadata(file, changeSet);
        updateMetadataTask
                .addOnSuccessListener(this,
                        new OnSuccessListener<Metadata>() {
                            @Override
                            public void onSuccess(Metadata metadata) {
                                showMessage(getString(R.string.custom_property_updated));
                                finish();
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unable to update metadata", e);
                        showMessage(getString(R.string.update_failed));
                        finish();
                    }
                });
        // [END update_custom_property]
    }
}
