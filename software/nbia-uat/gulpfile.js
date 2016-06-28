const gulp = require('gulp');
const del = require('del');
const typescript = require('gulp-typescript');
const tscConfig = require('./tsconfig.json');
const sourcemaps = require('gulp-sourcemaps');
const tslint = require('gulp-tslint');

// clean contents of dist dir
gulp.task('clean', function() {
   return del('dist/**/*');
});

gulp.task('copy:libs', ['copy:primeng'], function() {
   return gulp.src([
       "node_modules/es6-shim/es6-shim.min.js",
       "node_modules/systemjs/dist/system-polyfills.js",
       "node_modules/angular2/es6/dev/src/testing/shims_for_IE.js",
       "node_modules/angular2/bundles/angular2-polyfills.js",
       "node_modules/systemjs/dist/system.src.js",
       "node_modules/rxjs/bundles/Rx.js",
       "node_modules/angular2/bundles/angular2.dev.js",
       "node_modules/angular2/bundles/router.dev.js",
       "node_modules/angular2/bundles/http.dev.js",
	   "node_modules/angular2/bundles/browser.js",
	   "node_modules/angular2/core.js",
	   "node_modules/angular2/http.js",
	   "node_modules/angular2/compiler.js ",	   
	   "node_modules/angular2/platform/browser.js",
	   "node_modules/angular2/src/facade/lang.js",
	   "node_modules/angular2/src/core/angular_entrypoint.js",	   
	   "node_modules/angular2/src/platform/browser_common.js",	   
	   "node_modules/angular2/src/platform/browser/xhr_impl.js",	 	   
	   "node_modules/angular2/src/http/**/*.js",
	   "node_modules/angular2/src/core/di.js",
	   "node_modules/angular2/src/core/reflection/reflection_capabilities.js",
	   "node_modules/angular2/src/facade/lang.js",	 	   
	   "node_modules/angular2/router.js",
	   "node_modules/angular2/src/router/**/*.js",
       "node_modules/primeui/primeui-ng-all.min.js"
   ], {base: './node_modules'})
//       .pipe(gulp.dest('./dist/lib'));
		.pipe(gulp.dest('./dist/node_modules'));
});

gulp.task('copy:primeng', [], function() {
   return gulp.src([
       "node_modules/primeng/**/*"
   ], {base: './node_modules'})
//       .pipe(gulp.dest('./dist/lib'));
		.pipe(gulp.dest('./dist/node_modules'));
});

gulp.task('copy:everything', [], function() {
   return gulp.src([
       "node_modules/*/**/*"
   ], {base: './node_modules'})
       .pipe(gulp.dest('./dist/node_modules'));
});

gulp.task('copy:assets', [], function() {
   return gulp.src([
       'app/**/*',
       'index.html',
       'styles.css',
       'node_modules/primeui/themes/delta/theme.css',
       'node_modules/primeui/primeui-ng-all.min.css',
       'node_modules/primeui/themes/delta/images/ui-icons_222222_256x240.png',	   
       '!app/**/*.ts'
   ], {base: './'})
       .pipe(gulp.dest('./dist'));
});

gulp.task('tslint', function() {
   return gulp.src('app/**/*.ts')
       .pipe(tslint())
       .pipe(tslint.report('verbose'));
});

// typescript compile
gulp.task('compile', ['copy:libs', 'copy:assets'], function() {
   return gulp
       .src('app/**/*.ts')
       .pipe(typescript(tscConfig.compilerOptions))
       .pipe(sourcemaps.write('.'))
       .pipe(gulp.dest('./dist/app'));
});

gulp.task('build', ['compile']);
gulp.task('default', ['build']);
gulp.task('watch', function() {
    gulp.watch('app/**/*.ts', ['compile']);
    gulp.watch('app/**/*.{html,htm,css}', ['copy:assets']);
    gulp.watch('index.html', ['copy:assets']);
    gulp.watch('app/images/*', ['copy:assets']);
});