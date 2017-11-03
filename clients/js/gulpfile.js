const fs = require("fs");
const gulp = require("gulp");
const rename = require("gulp-rename");
const replace = require("gulp-replace");
const ts = require("gulp-typescript");
const tsProject = ts.createProject("tsconfig.json");
const uglify = require("gulp-uglify");

gulp.task("default", function () {
    return tsProject.src()
        .pipe(replace(
            "<BASE64_DATA_PLACEHOLDER>",
            fs.readFileSync("data/foo.dat", "base64")
        ))
        .pipe(tsProject())
        .js
        .pipe(gulp.dest("build")) // save human-readable JavaScript for Node
        .pipe(uglify({
            wrap: "googlei18n"
        }))
        .pipe(rename({
            extname: ".min.js"
        }))
        .pipe(gulp.dest("build")); // save minified JavaScript for browsers
});
