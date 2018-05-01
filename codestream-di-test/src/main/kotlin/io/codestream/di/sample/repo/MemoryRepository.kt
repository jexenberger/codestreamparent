package io.codestream.di.sample.repo

class MemoryRepository<T> : LinkedHashMap<String, T>()