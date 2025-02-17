/*
 * Copyright 2023-2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.fhir.sync.upload.request

import ca.uhn.fhir.context.FhirContext
import com.google.android.fhir.ContentTypes
import com.google.android.fhir.sync.upload.patch.Patch
import com.google.android.fhir.sync.upload.patch.PatchMapping
import com.google.android.fhir.sync.upload.patch.StronglyConnectedPatchMappings
import org.hl7.fhir.r4.model.Binary
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.codesystems.HttpVerb

/** Generates list of [UrlUploadRequest]s for a list of [Patch]es. */
internal class UrlRequestGenerator(
  private val getUrlRequestForPatch: (patch: Patch) -> UrlUploadRequest,
) : UploadRequestGenerator {

  /**
   * Since a [UrlUploadRequest] can only handle a single resource request, the
   * [StronglyConnectedPatchMappings.patchMappings] are flattened and handled as acyclic mapping to
   * generate [UrlUploadRequestMapping] for each [PatchMapping].
   *
   * **NOTE**
   *
   * Since the referential integrity on the sever may get violated if the subsequent requests have
   * cyclic dependency on each other, We may introduce configuration for application to provide
   * server's referential integrity settings and make it illegal to generate [UrlUploadRequest] when
   * server has strict referential integrity and the requests have cyclic dependency amongst itself.
   */
  override fun generateUploadRequests(
    mappedPatches: List<StronglyConnectedPatchMappings>,
  ): List<UrlUploadRequestMapping> =
    mappedPatches
      .map { it.patchMappings }
      .flatten()
      .map {
        UrlUploadRequestMapping(
          localChanges = it.localChanges,
          generatedRequest = getUrlRequestForPatch(it.generatedPatch),
        )
      }

  companion object Factory {

    private val createMapping =
      mapOf(
        HttpVerb.POST to this::postForCreateResource,
        HttpVerb.PUT to this::putForCreateResource,
      )

    private val updateMapping =
      mapOf(
        HttpVerb.PATCH to this::patchForUpdateResource,
      )

    fun getDefault() = getGenerator(HttpVerb.PUT, HttpVerb.PATCH)

    /**
     * Returns a [UrlRequestGenerator] based on the provided [HttpVerb]s for creating and updating
     * resources. The function may throw an [IllegalArgumentException] if the provided [HttpVerb]s
     * are not supported.
     */
    fun getGenerator(
      httpVerbToUseForCreate: HttpVerb,
      httpVerbToUseForUpdate: HttpVerb,
    ): UrlRequestGenerator {
      val createFunction =
        createMapping[httpVerbToUseForCreate]
          ?: throw IllegalArgumentException(
            "Creation using $httpVerbToUseForCreate is not supported.",
          )

      val updateFunction =
        updateMapping[httpVerbToUseForUpdate]
          ?: throw IllegalArgumentException(
            "Update using $httpVerbToUseForUpdate is not supported.",
          )

      return UrlRequestGenerator { patch ->
        when (patch.type) {
          Patch.Type.INSERT -> createFunction(patch)
          Patch.Type.UPDATE -> updateFunction(patch)
          Patch.Type.DELETE -> deleteFunction(patch)
        }
      }
    }

    private fun deleteFunction(patch: Patch) =
      UrlUploadRequest(
        httpVerb = HttpVerb.DELETE,
        url = "${patch.resourceType}/${patch.resourceId}",
        resource =
          FhirContext.forR4Cached().newJsonParser().parseResource(patch.payload) as Resource,
      )

    private fun postForCreateResource(patch: Patch) =
      UrlUploadRequest(
        httpVerb = HttpVerb.POST,
        url = patch.resourceType,
        resource =
          FhirContext.forR4Cached().newJsonParser().parseResource(patch.payload) as Resource,
      )

    private fun putForCreateResource(patch: Patch) =
      UrlUploadRequest(
        httpVerb = HttpVerb.PUT,
        url = "${patch.resourceType}/${patch.resourceId}",
        resource =
          FhirContext.forR4Cached().newJsonParser().parseResource(patch.payload) as Resource,
      )

    private fun patchForUpdateResource(patch: Patch) =
      UrlUploadRequest(
        httpVerb = HttpVerb.PATCH,
        url = "${patch.resourceType}/${patch.resourceId}",
        resource = Binary().apply { data = patch.payload.toByteArray() },
        headers = mapOf("Content-Type" to ContentTypes.APPLICATION_JSON_PATCH),
      )
  }
}
