      function testZ2U() {
          // Run through all zawygi_unicode_convert_data elements
          // with Z -> U, comparing with expected.
          // Clear
          var failArea = document.getElementById("failure_out");
          failArea.value = failArea.innerHTML = "";

          var numFailed = 0;
          var numTests = 0;
          for (var i in zawygi_unicode_convert_data) {
              var z = zawygi_unicode_convert_data[i].z;
              var u = zawygi_unicode_convert_data[i].u;

              var outUText = Z2Utransliterate(z);
              if (u != outUText) {
                  var failMsg = "Z to U: Failed to convert " + i + ".\n  Expected: " + u +
                        "\n    but got: " + outUText +
                       "\n  Z input = " + z + "\n\n";
                  failArea.value += failMsg;
//                  alert(failMsg);
                  numFailed += 1;
              }
              numTests += 1;
          }
          alert(numTests + " total run with " + numFailed + " failing.");
      }

      function testU2Z() {
          var numFailed = 0;
          var numTests = 0;

          for (var i in zawygi_unicode_convert_data) {
              z = zawygi_unicode_convert_data[i].z;
              u = zawygi_unicode_convert_data[i].u;

              var outZText = transliterate(u);
              var zNorm = ZNormTransliterate(outZText);
              var zOutNorm = ZNormTransliterate(outZText);
              if (zNorm != zOutNorm) {
                  alert("U to Z: Failed to convert " + i + ".\n  Expected: " + zNorm +
                        "\n     but got: " + zOutNorm +
                        "\n  Z input = " + z);
                  numFailed += 1;
              }
              numTests += 1;
          }
          alert(numTests + " total run with " + numFailed + " failing.");
      }
