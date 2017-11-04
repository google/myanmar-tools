var fs = require("fs");
var clean = require("gulp-clean");
var gulp = require("gulp");
var jasmine = require("gulp-jasmine");
var jasmineBrowser = require("gulp-jasmine-browser");
var preprocess = require("gulp-preprocess");
var rename = require("gulp-rename");
var replace = require("gulp-replace");
var ts = require("gulp-typescript");
var uglify = require("gulp-uglify");

// Helper function to create a non-minified JavaScript stream.
function typescriptToJavascript() {
    var tsProject = ts.createProject("tsconfig.json");
    return tsProject.src()
        .pipe(replace(
            "<BASE64_DATA_PLACEHOLDER>",
            fs.readFileSync("resources/zawgyiUnicodeModel.dat", "base64")
        ))
        .pipe(tsProject())
        .js;
}

gulp.task("build-node", function () {
    return typescriptToJavascript()
        .pipe(preprocess({
            context: { NODEJS: true }
        }))
        .pipe(gulp.dest("build"));
});

gulp.task("build-browser", function () {
    return typescriptToJavascript()
        .pipe(preprocess({
            context: { NODEJS: false }
        }))
        .pipe(uglify({
            wrap: "google_myanmar_tools"
        }))
        .pipe(rename({
            extname: ".min.js"
        }))
        .pipe(gulp.dest("build"));
});

gulp.task("test-node", ["build-node"], function () {
    return gulp.src("spec/**/*_spec.js")
        .pipe(jasmine());
});

gulp.task("test-browser", ["build-browser"], function () {
    return gulp.src([
        "build/zawgyi_detector.min.js",
        "resources/compatibility.tsv",
        "spec/**/*_spec.js"])
        .pipe(jasmineBrowser.specRunner({ console: true }))
        .pipe(jasmineBrowser.headless({ driver: "phantomjs" }));
});

gulp.task("clean", function () {
    return gulp.src("build").pipe(clean());
});

gulp.task("test", ["test-node", "test-browser"]);
gulp.task("default", ["build-node", "build-browser"]);
