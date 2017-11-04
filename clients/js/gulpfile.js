const fs = require("fs");
const clone = require("gulp-clone");
const gulp = require("gulp");
const preprocess = require("gulp-preprocess");
const rename = require("gulp-rename");
const replace = require("gulp-replace");
const ts = require("gulp-typescript");
const tsProject = ts.createProject("tsconfig.json");
const uglify = require("gulp-uglify");

gulp.task("default", function () {
    var js = tsProject.src()
        .pipe(replace(
            "<BASE64_DATA_PLACEHOLDER>",
            fs.readFileSync("resources/zawgyiUnicodeModel.dat", "base64")
        ))
        .pipe(tsProject())
        .js;

    // Save human-readable JavaScript for Node
    js.pipe(clone())
        .pipe(preprocess({
            context: { NODEJS: true }
        }))
        .pipe(gulp.dest("build"));

    // Save minified JavaScript for browsers
    js
        .pipe(preprocess({
            context: { NODEJS: false }
        }))
        .pipe(uglify({
            wrap: "googlei18n"
        }))
        .pipe(rename({
            extname: ".min.js"
        }))
        .pipe(gulp.dest("build"));
});
