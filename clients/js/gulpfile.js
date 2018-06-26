/* Copyright 2017 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var fs = require("fs");
var clean = require("gulp-clean");
var gulp = require("gulp");
var jasmine = require("gulp-jasmine");
var jasmineBrowser = require("gulp-jasmine-browser");
var prependText = require("gulp-append-prepend").prependText;
var preprocess = require("gulp-preprocess");
var rename = require("gulp-rename");
var replace = require("gulp-replace");
var sourcemaps = require("gulp-sourcemaps");
var ts = require("gulp-typescript");
var uglify = require("gulp-uglify");

var LICENSE = `/* Copyright 2017 Google LLC
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/`;

// Helper function to create a non-minified JavaScript stream.
function typescriptToJavascript() {
    var tsProject = ts.createProject("tsconfig.json");
    return tsProject.src()
        .pipe(replace(
            "<BASE64_DATA_PLACEHOLDER>",
            fs.readFileSync("resources/zawgyiUnicodeModel.dat", "base64")
        ))
        .pipe(replace(
            "declare function getAllRulesZ2U(): TranslitRule[][]; // PLACEHOLDER",
            fs.readFileSync("resources/Z2U.js", "utf8")
        ))
        .pipe(replace(
            "declare function getAllRulesU2Z(): TranslitRule[][]; // PLACEHOLDER",
            fs.readFileSync("resources/U2Z.js", "utf8")
        ))
        .pipe(replace(
            "declare function getAllRulesZNorm(): TranslitRule[][]; // PLACEHOLDER",
            fs.readFileSync("resources/ZNorm.js", "utf8")
        ))
        .pipe(tsProject())
        .js;
}

gulp.task("build-node", function () {
    return typescriptToJavascript()
        .pipe(preprocess({
            context: { NODEJS: true }
        }))
        .pipe(gulp.dest("build_node"));
});

gulp.task("build-browser-full", function () {
    // Non-minified browser build
    return typescriptToJavascript()
        .pipe(preprocess({
            context: { NODEJS: false }
        }))
        .pipe(uglify({
            wrap: "google_myanmar_tools",
            mangle: false,
            compress: false,
            output: { beautify: true }
        }))
        .pipe(prependText(LICENSE, "\n"))
        .pipe(gulp.dest("build_browser"));
});

gulp.task("build-browser-min", function () {
    // Minified browser build
    return typescriptToJavascript()
        .pipe(sourcemaps.init())
        .pipe(preprocess({
            context: { NODEJS: false }
        }))
        .pipe(uglify({
            wrap: "google_myanmar_tools"
        }))
        .pipe(prependText(LICENSE, "\n"))
        .pipe(rename({
            extname: ".min.js"
        }))
        .pipe(sourcemaps.write("."))
        .pipe(gulp.dest("build_browser"));
});

gulp.task("build-browser", gulp.parallel("build-browser-full", "build-browser-min"));

gulp.task("test-node", gulp.series("build-node", function () {
    return gulp.src("spec/**/*_spec.js")
        .pipe(jasmine());
}));

gulp.task("test-browser-full", function () {
    return gulp.src([
        "build_browser/zawgyi_converter.js",
        "build_browser/zawgyi_detector.js",
        "resources/compatibility.tsv",
        "spec/**/*_spec.js"])
        .pipe(jasmineBrowser.specRunner({ console: true }))
        .pipe(jasmineBrowser.headless({ driver: "phantomjs", port: 8001 }));
});

gulp.task("test-browser-min", function () {
    return gulp.src([
        "build_browser/zawgyi_converter.min.js",
        "build_browser/zawgyi_detector.min.js",
        "resources/compatibility.tsv",
        "spec/**/*_spec.js"])
        .pipe(jasmineBrowser.specRunner({ console: true }))
        .pipe(jasmineBrowser.headless({ driver: "phantomjs", port: 8002 }));
});

gulp.task("test-browser", gulp.series(
    "build-browser",
    "test-browser-full",
    "test-browser-min"
));

gulp.task("clean", function () {
    return gulp.src("build").pipe(clean());
});

gulp.task("default", gulp.parallel("build-node", "build-browser"));
gulp.task("test", gulp.series("test-node", "test-browser"));
